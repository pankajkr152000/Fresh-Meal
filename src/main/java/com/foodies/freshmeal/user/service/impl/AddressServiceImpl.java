package com.foodies.freshmeal.user.service.impl;

import org.springframework.stereotype.Service;

import com.foodies.freshmeal.common.constants.RoleType;
import com.foodies.freshmeal.common.constants.SequenceConstants;
import com.foodies.freshmeal.common.date.AppCalendar;
import com.foodies.freshmeal.common.enums.EntityName;
import com.foodies.freshmeal.common.exception.CommonErrorConstants;
import com.foodies.freshmeal.common.exception.ResourceNotFoundException;
import com.foodies.freshmeal.common.factory.EntityFactory;
import com.foodies.freshmeal.common.io.DataContext;
import com.foodies.freshmeal.common.io.service.IServiceContext;
import com.foodies.freshmeal.common.io.service.IServiceInput;
import com.foodies.freshmeal.common.io.service.IServiceOutput;
import com.foodies.freshmeal.common.io.service.impl.ServiceInput;
import com.foodies.freshmeal.common.io.service.impl.ServiceOutput;
import com.foodies.freshmeal.common.sequence.service.IDatabaseSequenceService;
import com.foodies.freshmeal.pincode.dto.PincodeLookupRequest;
import com.foodies.freshmeal.pincode.service.IPincodeService;
import com.foodies.freshmeal.pincode.valueObject.PincodeDetails;
import com.foodies.freshmeal.user.dto.AddressIdRequest;
import com.foodies.freshmeal.user.dto.AddressInputDTO;
import com.foodies.freshmeal.user.dto.AddressRequest;
import com.foodies.freshmeal.user.dto.AddressResponse;
import com.foodies.freshmeal.user.entity.AddressEntity;
import com.foodies.freshmeal.user.repository.IAddressRepository;
import com.foodies.freshmeal.user.service.IAddressService;

/**
 * =============================================================================
 * Address Service Implementation
 * =============================================================================
 *
 * Responsibilities ---------------- • Load an existing address. • Generate
 * Address ID. • Generate Address Number. • Create AddressEntity. • Resolve
 * location information through PincodeService. • Persist the address. • Return
 * AddressResponse.
 *
 * The service follows the common FreshMeal IServiceInput / IServiceOutput
 * architecture.
 * =============================================================================
 */
@Service
public class AddressServiceImpl implements IAddressService {

    private final IDatabaseSequenceService databaseSequenceService;

    private final IAddressRepository addressRepository;

    private final IPincodeService pincodeService;

    private final IServiceContext serviceContext;

    public AddressServiceImpl(IDatabaseSequenceService databaseSequenceService, IAddressRepository addressRepository,
            IPincodeService pincodeService, IServiceContext serviceContext) {

        this.databaseSequenceService = databaseSequenceService;
        this.addressRepository = addressRepository;
        this.pincodeService = pincodeService;
        this.serviceContext = serviceContext;
    }

    /**
     * Loads an address using its business-facing address number.
     *
     * @param input address identifier input
     * @return address entity
     */
    @Override
    public IServiceOutput<AddressEntity> loadAddress(IServiceInput<AddressIdRequest> input) {

        AddressIdRequest addressRequest = input.getInput();

        AddressEntity addressEntity = addressRepository.findById(addressRequest.getAddressId())
                .orElseThrow(() -> new ResourceNotFoundException(CommonErrorConstants.RESOURCE_NOT_FOUND));

        return new ServiceOutput<>(addressEntity);
    }

    /**
     * Generates the MongoDB/business entity ID for the address.
     *
     * @param input address creation input
     * @return generated address ID
     */
    @Override
    public IServiceOutput<String> generateAddressId(IServiceInput<AddressInputDTO> input) {

        long seq = databaseSequenceService.generateSequence(input.getServiceContext(),
                SequenceConstants.ADDRESS_SEQUENCE);

        IServiceContext inputServiceContext = input.getServiceContext();

        inputServiceContext.setAttribute(DataContext.ADDRESS_SEQUENCE, seq);

        String addressId = String.format(SequenceConstants.ADDRESS_DB_ID_PATTERN, seq);

        IServiceOutput<String> output = new ServiceOutput<>();

        output.setOutput(addressId);

        return output;
    }

    /**
     * Generates the business-facing address number.
     *
     * The sequence generated while creating the address ID is reused so that both
     * identifiers belong to the same address sequence.
     *
     * @param input address creation input
     * @return generated address number
     */
    @Override
    public IServiceOutput<String> generateAddressNumber(IServiceInput<AddressInputDTO> input) {

        long seq;

        if (input.getServiceContext().hasAttribute(DataContext.ADDRESS_SEQUENCE)) {
            seq = (Long) input.getServiceContext().getAttribute(DataContext.ADDRESS_SEQUENCE);
        } else {
            seq = databaseSequenceService.getCurrentSequence(input.getServiceContext(),
                    SequenceConstants.ADDRESS_SEQUENCE);
        }

        String addressNumber = String.format(SequenceConstants.ADDRESS_NUMBER_PATTERN, seq);

        IServiceOutput<String> output = new ServiceOutput<>();

        output.setOutput(addressNumber);

        return output;
    }

    /**
     * Creates and persists an AddressEntity.
     *
     * @param input address creation input
     * @return created address entity
     */
    @Override
    public IServiceOutput<AddressEntity> createAddressEntity(IServiceInput<AddressInputDTO> input) {

        AddressEntity addressEntity = (AddressEntity) EntityFactory.createEntity(EntityName.ADDRESS_ENTITY);
        AddressInputDTO addressInputDTO = input.getInput();
        AddressRequest addressRequest = addressInputDTO.getAddressRequest();

        IServiceInput<AddressInputDTO> addressServiceInput = new ServiceInput<>();

        AddressInputDTO createAddressInputDTO = new AddressInputDTO();

        createAddressInputDTO.setAddressRequest(addressRequest);

        addressServiceInput.setInput(createAddressInputDTO);

        // Generate Address ID.
        String addressId = generateAddressId(addressServiceInput).getOutput();

        addressEntity.setId(addressId);

        // Generate Address Number.
        String addressNumber = generateAddressNumber(addressServiceInput).getOutput();

        addressEntity.setAddressNumber(addressNumber);

        /*
         * Resolve country, state and district from the supplied pincode.
         *
         * These values must come from the PincodeService rather than being trusted from
         * the client request.
         */
        PincodeLookupRequest pincodeLookupRequest = new PincodeLookupRequest(addressRequest.getPostalCode());

        IServiceInput<PincodeLookupRequest> pincodeInput = new ServiceInput<>();

        pincodeInput.setInput(pincodeLookupRequest);

        IServiceOutput<PincodeDetails> pincodeOutput = pincodeService.getPincodeDetails(pincodeInput);

        PincodeDetails pincodeDetails = pincodeOutput.getOutput();

        addressEntity.setPostalCode(pincodeDetails.pincode());
        addressEntity.setCountry(pincodeDetails.country());
        addressEntity.setState(pincodeDetails.state());
        addressEntity.setDistrict(pincodeDetails.district());

        // Address information supplied by the client.
        addressEntity.setAddressType(addressRequest.getAddressType());
        addressEntity.setDefaultAddress(addressRequest.isDefaultAddress());
        addressEntity.setRecipientName(addressRequest.getRecipientName());
        addressEntity.setPhoneNumber(addressRequest.getPhoneNumber());
        addressEntity.setAddressLine1(addressRequest.getAddressLine1());
        addressEntity.setAddressLine2(addressRequest.getAddressLine2());
        addressEntity.setLandmark(addressRequest.getLandmark());
        addressEntity.setCity(addressRequest.getCity());
        addressEntity.setCreatedAt(AppCalendar.getBusinessLocalDateTime());

        if (serviceContext.getUserProfile() != null) {
            addressEntity.setCreatedBy(serviceContext.getUserProfile().getId());
            addressEntity.setUserNumber(serviceContext.getUserProfile().getId());
        } else {
            addressEntity.setCreatedBy(RoleType.ADMIN.getLabel());
        }
        addressRepository.save(addressEntity);

        IServiceOutput<AddressEntity> output = new ServiceOutput<>();
        output.setOutput(addressEntity);

        return output;
    }

    /**
     * Creates a customer address and converts it into AddressResponse.
     *
     * @param input address creation input
     * @return created address response
     */
    @Override
    public IServiceOutput<AddressResponse> addAddress(IServiceInput<AddressInputDTO> input) {

        IServiceOutput<AddressEntity> entityOutput = createAddressEntity(input);
        AddressEntity addressEntity = entityOutput.getOutput();

        AddressResponse response = new AddressResponse();
        response.setAddressNumber(addressEntity.getAddressNumber());
        response.setAddressType(addressEntity.getAddressType());
        response.setDefaultAddress(addressEntity.isDefaultAddress());
        response.setRecipientName(addressEntity.getRecipientName());
        response.setPhoneNumber(addressEntity.getPhoneNumber());
        response.setAddressLine1(addressEntity.getAddressLine1());
        response.setAddressLine2(addressEntity.getAddressLine2());
        response.setLandmark(addressEntity.getLandmark());
        response.setCity(addressEntity.getCity());
        response.setDistrict(addressEntity.getDistrict());
        response.setState(addressEntity.getState());
        response.setCountry(addressEntity.getCountry());
        response.setPostalCode(addressEntity.getPostalCode());
        response.setLocation(addressEntity.getLocation());

        IServiceOutput<AddressResponse> output = new ServiceOutput<>();
        output.setOutput(response);
        return output;
    }
}