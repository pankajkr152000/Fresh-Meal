package com.foodies.freshmeal.user.service.impl;

import java.util.List;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.exception.BusinessException;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.RepositoryContext;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.service.IPincodeService;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;
import com.foodies.freshmeal.user.constants.AddressErrorConstants;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressNumberRequest;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.dto.AddressUpdateRequest;
import com.foodies.freshmeal.user.entity.AddressEntity;
import com.foodies.freshmeal.user.entity.UserProfile;
import com.foodies.freshmeal.user.mapper.AddressMapper;
import com.foodies.freshmeal.user.repository.IAddressRepository;
import com.foodies.freshmeal.user.service.IAddressService;
import com.foodies.freshmeal.user.validation.AddressValidator;

/**
 * ============================================================================
 * Service : AddressServiceImpl
 * ============================================================================
 *
 * Implements customer address management operations.
 *
 * <p>
 * This service is responsible for:
 * </p>
 *
 * <ul>
 * <li>Address creation and persistence.</li>
 * <li>Address ownership validation.</li>
 * <li>Business identifier generation.</li>
 * <li>Pincode-based location resolution.</li>
 * <li>Address updates.</li>
 * <li>Default-address management.</li>
 * <li>Address soft deletion.</li>
 * <li>Conversion of entities to response DTOs through AddressMapper.</li>
 * </ul>
 *
 * <p>
 * The service does not trust client-provided ownership or pincode-derived
 * location information.
 * </p>
 *
 * @author Pankaj Kumar
 * @since 1.0
 */
@Service
@Transactional
public class AddressServiceImpl implements IAddressService {

    private final IDatabaseSequenceService databaseSequenceService;
    private final IAddressRepository addressRepository;
    private final IPincodeService pincodeService;
    private final IServiceContext serviceContext;
    private final AddressMapper addressMapper;
    private final AddressValidator addressValidator;

    /**
     * Creates an AddressServiceImpl.
     *
     * @param databaseSequenceService database sequence service
     * @param addressRepository       address repository
     * @param pincodeService          pincode lookup service
     * @param serviceContext          request-scoped service context
     * @param addressMapper           address mapper
     * @param addressValidator        address validator
     */
    public AddressServiceImpl(
            final IDatabaseSequenceService databaseSequenceService,
            final IAddressRepository addressRepository,
            final IPincodeService pincodeService,
            final IServiceContext serviceContext,
            final AddressMapper addressMapper,
            final AddressValidator addressValidator) {

        this.databaseSequenceService = databaseSequenceService;
        this.addressRepository = addressRepository;
        this.pincodeService = pincodeService;
        this.serviceContext = serviceContext;
        this.addressMapper = addressMapper;
        this.addressValidator = addressValidator;
    }

    /**
     * Creates and persists a customer address.
     *
     * @param input address creation input
     * @return created address response
     */
    @Override
    public IServiceOutput<AddressResponse> addAddress(
            final IServiceInput<AddressInputDTO> input) {

        validateServiceInput(input);

        final AddressInputDTO addressInput = input.getInput();

        if (addressInput == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_REQUEST_REQUIRED);
        }

        final AddressRequest request = addressInput.getAddressRequest();

        addressValidator.validateCreateRequest(request);

        final UserProfile userProfile = resolveAuthenticatedUser();

        final AddressEntity addressEntity = AddressEntity.create() instanceof AddressEntity
                ? (AddressEntity) AddressEntity.create()
                : null;

        if (addressEntity == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_OPERATION_NOT_ALLOWED);
        }

        final long sequence = generateSequence(input.getServiceContext());

        addressEntity.setId(
                String.format(
                        SequenceConstants.ADDRESS_DB_ID_PATTERN,
                        sequence));

        addressEntity.setAddressNumber(
                String.format(
                        SequenceConstants.ADDRESS_NUMBER_PATTERN,
                        sequence));

        addressEntity.setUserNumber(userProfile.getUserNumber());
        addressEntity.setCreatedBy(userProfile.getUserNumber());
        addressEntity.setCreatedAt(
                AppCalendar.getBusinessLocalDateTime());

        addressMapper.mapRequestToEntity(request, addressEntity);

        resolvePincode(addressEntity, request.getPostalCode());

        if (addressEntity.isDefaultAddress()) {
            clearExistingDefaultAddress(
                    userProfile.getUserNumber(),
                    null);
        }

        final AddressEntity savedEntity = addressRepository.save(addressEntity);

        return output(addressMapper.toResponse(savedEntity));
    }

    /**
     * Retrieves an address using its business-facing address number.
     *
     * @param input address number request
     * @return address response
     */
    @Override
    public IServiceOutput<AddressResponse> getByAddressNumber(
            final IServiceInput<AddressNumberRequest> input) {

        validateServiceInput(input);

        addressValidator.validateAddressNumberRequest(input.getInput());

        final UserProfile userProfile = resolveAuthenticatedUser();

        final AddressEntity addressEntity = loadActiveByAddressNumber(
                input.getInput().getAddressNumber());

        validateOwnership(userProfile, addressEntity);

        return output(addressMapper.toResponse(addressEntity));
    }

    /**
     * Retrieves all active addresses belonging to the authenticated user.
     *
     * @param input service input
     * @return active customer addresses
     */
    @Override
    public IServiceOutput<List<AddressResponse>> getMyAddresses(
            final IServiceInput<Void> input) {

        validateServiceInput(input);

        final UserProfile userProfile = resolveAuthenticatedUser();

        final Query query = Query.query(
                Criteria.where("userNumber")
                        .is(userProfile.getUserNumber()));

        final List<AddressEntity> addresses = addressRepository.findAll(query);

        return output(addressMapper.toResponseList(addresses));
    }

    /**
     * Updates an existing customer address.
     *
     * @param input address update input
     * @return updated address response
     */
    @Override
    public IServiceOutput<AddressResponse> updateAddress(
            final IServiceInput<AddressUpdateRequest> input) {

        validateServiceInput(input);

        final AddressUpdateRequest request = input.getInput();

        addressValidator.validateUpdateRequest(request);

        final UserProfile userProfile = resolveAuthenticatedUser();

        final AddressEntity addressEntity = loadActiveByAddressNumber(request.getAddressNumber());

        validateOwnership(userProfile, addressEntity);

        addressMapper.mapUpdateRequestToEntity(
                request,
                addressEntity);

        resolvePincode(
                addressEntity,
                request.getPostalCode());

        addressEntity.setUpdatedAt(
                AppCalendar.getBusinessLocalDateTime());

        addressEntity.setUpdatedBy(
                userProfile.getUserNumber());

        final AddressEntity savedEntity = addressRepository.save(addressEntity);

        return output(addressMapper.toResponse(savedEntity));
    }

    /**
     * Sets an address as the default address of the authenticated user.
     *
     * @param input address number request
     * @return updated default address response
     */
    @Override
    public IServiceOutput<AddressResponse> setDefaultAddress(
            final IServiceInput<AddressNumberRequest> input) {

        validateServiceInput(input);

        addressValidator.validateAddressNumberRequest(input.getInput());

        final UserProfile userProfile = resolveAuthenticatedUser();

        final AddressEntity addressEntity = loadActiveByAddressNumber(
                input.getInput().getAddressNumber());

        validateOwnership(userProfile, addressEntity);

        if (!addressEntity.isDefaultAddress()) {

            clearExistingDefaultAddress(
                    userProfile.getUserNumber(),
                    addressEntity.getAddressNumber());

            addressEntity.setDefaultAddress(true);
            addressEntity.setUpdatedAt(
                    AppCalendar.getBusinessLocalDateTime());
            addressEntity.setUpdatedBy(
                    userProfile.getUserNumber());

            addressRepository.save(addressEntity);
        }

        return output(addressMapper.toResponse(addressEntity));
    }

    /**
     * Soft deletes an address belonging to the authenticated user.
     *
     * @param input address number request
     * @return empty service output
     */
    @Override
    public IServiceOutput<Void> deleteAddress(
            final IServiceInput<AddressNumberRequest> input) {

        validateServiceInput(input);

        addressValidator.validateAddressNumberRequest(input.getInput());

        final UserProfile userProfile = resolveAuthenticatedUser();

        final AddressEntity addressEntity = loadActiveByAddressNumber(
                input.getInput().getAddressNumber());

        validateOwnership(userProfile, addressEntity);

        addressRepository.softDelete(
                addressEntity.getId(),
                RepositoryContext.of(userProfile.getUserNumber()));

        return output(null);
    }

    /**
     * Generates the address sequence.
     *
     * @param context service context
     * @return generated sequence
     */
    private long generateSequence(
            final IServiceContext context) {

        return databaseSequenceService.generateSequence(
                context,
                SequenceConstants.ADDRESS_SEQUENCE);
    }

    /**
     * Resolves pincode-derived address information.
     *
     * @param addressEntity address entity
     * @param postalCode    postal code
     */
    private void resolvePincode(
            final AddressEntity addressEntity,
            final String postalCode) {

        final PincodeLookupRequest request = new PincodeLookupRequest(postalCode);

        final IServiceInput<PincodeLookupRequest> input = new ServiceInput<>();

        input.setInput(request);
        input.setServiceContext(serviceContext);

        final IServiceOutput<PincodeDetails> output = pincodeService.getPincodeDetails(input);

        final PincodeDetails details = output.getOutput();

        if (details == null) {
            throw new BusinessException(
                    AddressErrorConstants.INVALID_PINCODE);
        }

        addressEntity.setPostalCode(details.pincode());
        addressEntity.setCountry(details.country());
        addressEntity.setState(details.state());
        addressEntity.setDistrict(details.district());
    }

    /**
     * Loads an active address using its business-facing address number.
     *
     * @param addressNumber business address number
     * @return active address entity
     */
    private AddressEntity loadActiveByAddressNumber(
            final String addressNumber) {

        final Query query = Query.query(
                Criteria.where("addressNumber")
                        .is(addressNumber));

        return addressRepository.findOne(query)
                .orElseThrow(() -> new BusinessException(
                        AddressErrorConstants.ADDRESS_NOT_FOUND));
    }

    /**
     * Ensures that only one active address belonging to a user is marked as
     * the default address.
     *
     * @param userNumber            owner user number
     * @param excludedAddressNumber address number that should not be changed
     */
    private void clearExistingDefaultAddress(
            final String userNumber,
            final String excludedAddressNumber) {

        final Query query = Query.query(
                Criteria.where("userNumber")
                        .is(userNumber)
                        .and("defaultAddress")
                        .is(true));

        final List<AddressEntity> addresses = addressRepository.findAll(query);

        for (final AddressEntity address : addresses) {

            if (excludedAddressNumber != null
                    && excludedAddressNumber.equals(
                            address.getAddressNumber())) {
                continue;
            }

            address.setDefaultAddress(false);
            address.setUpdatedAt(
                    AppCalendar.getBusinessLocalDateTime());
            address.setUpdatedBy(userNumber);

            addressRepository.save(address);
        }
    }

    /**
     * Resolves the authenticated user from the request-scoped service context.
     *
     * @return authenticated user profile
     */
    private UserProfile resolveAuthenticatedUser() {

        if (serviceContext == null
                || serviceContext.getUserProfile() == null
                || serviceContext.getUserProfile().getUserNumber() == null
                || serviceContext.getUserProfile()
                        .getUserNumber()
                        .isBlank()) {

            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_OPERATION_NOT_ALLOWED);
        }

        return serviceContext.getUserProfile();
    }

    /**
     * Validates that the authenticated user owns the specified address.
     *
     * @param userProfile   authenticated user
     * @param addressEntity address entity
     */
    private void validateOwnership(
            final UserProfile userProfile,
            final AddressEntity addressEntity) {

        if (!userProfile.getUserNumber()
                .equals(addressEntity.getUserNumber())) {

            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_OPERATION_NOT_ALLOWED);
        }
    }

    /**
     * Validates common service input.
     *
     * @param input service input
     */
    private void validateServiceInput(
            final IServiceInput<?> input) {

        if (input == null) {
            throw new BusinessException(
                    AddressErrorConstants.ADDRESS_REQUEST_REQUIRED);
        }
    }

    /**
     * Creates a service output containing the supplied value.
     *
     * @param value output value
     * @param <T>   output type
     * @return service output
     */
    private <T> IServiceOutput<T> output(final T value) {

        final IServiceOutput<T> output = new ServiceOutput<>();
        output.setOutput(value);
        return output;
    }
    // TODO: after this checkout i will next refactor address to get current address
    // from that we can get get exact or
    // TODO: approximate geolocation from that we have correct longitude and
    // latitude so rain may apply duration and direction we calculate
}