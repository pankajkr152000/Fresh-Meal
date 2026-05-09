package com.foodies.freshmeal.exception;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


public class SystemConfigBean {

	private static final Log logger = LogFactory.getLog(SystemConfigBean.class);
	
	private static final String YES_INDICATOR_CODE = "Y";

	private ApplicationContext applicationContext;

	private static Document systemConfigXmlDoc;

	// Added by Alok, CR# - COM#03220
	private static SystemConfigBean systemConfigBean;

	private static String urlPrf = null;

	private static HashMap<String, Object> systemConfigMap;

	public static final String CFG_STARTUP_MODE_LOCATION = "/resource/Startup-Mode-{0}.xml";
	
	public static final String BEAN_NAME ="@name";
	
	public static final String LAZY_LOAD = "@lazy-load";
	
	public static final String LAZY_LOAD_BEAN = "//startup-mode/bean[@name=''{0}'']/@lazy-load";
	
	public static final String BEAN_DEFINITION = "//startup-mode/bean";
	
	public static final String STARTUP_MODE = "//Config/StartUpConfigMode/StartUpMode";
	
	public static final String STARTUP_MODE_FILE = "//Config/StartUpConfigMode/StartUpModeFile";
	
	public static final String DEFAULT_STARTUP_MODE = "PROD";
	
	public static final String JSPTITLE_CHANGE="//Config/JspTitle/JspTitleChange";
	public static final String LANGUAGE_LIST = "//Config/LanguageSettings/Language";
	
	public static final String PROSPECTIVE_PRICE_FILESYSTEM_BEAN_KEY = "//Config/installation/prospectivePrice[@site=\"{0}\"]";
	
	public static final String FUND_STATUS_MATRIX_APPL_FILESYSTEM_BEAN_KEY = "//Config/installation/fundStatusMatrixApplicable[@site=\"{0}\"]";
	
	public static final String MULTICURRENCY_ENABLED_FILESYSTEM_BEAN_KEY = "//Config/installation/multiCurrency[@tenant=\"{0}\"]";
	
	public static final String SYSTEM_USER_FILESYSTEM_BEAN_KEY = "//Config/installation/tenant-system-user[@tenant=\"{0}\"]";
	
	public static final String DOC_ENV_BAT_CM_REPO_ID_DEFAULT="//Config/configEnvParameter[@applicableFor=\"DocumentManagement\" and @tenant=\"{0}\" and @default=\"true\"]/@repositoryId";
	
	public static final String DOC_ENV_BAT_CM_REPO_TYPE_DEFAULT="//Config/configEnvParameter[@applicableFor=\"DocumentManagement\" and @tenant=\"{0}\" and @default=\"true\"]/@repositoryType";
	
	public static final String DOC_ENV_BAT_CM_DOC_TYPE_DEFAULT="//Config/configEnvParameter[@applicableFor=\"DocumentManagement\" and @tenant=\"{0}\" and @default=\"true\"]/parameter[@name=\"DocumentType\"]";
	
	public static final String EXECUTABLE_PATH = "//Config/AllowableExecutables/Executable[@name=\"{0}\"]";
	
	private Document startupModeDoc;
	
	private static final Boolean isMapBasedCacheEnabled = true;
	
	static {
		getInstance();
	}
	
	

	public static SystemConfigBean getInstance() {
		if (systemConfigBean == null) {
			systemConfigBean = new SystemConfigBean();
			if (systemConfigXmlDoc == null) {
				
				// Added by Alok, CR# - COM#03220
				systemConfigMap = new HashMap<String, Object>(512);
				// Added by Amit, To Enable Startup Mode
				systemConfigBean.initStartupMode();
			}
		}
		return systemConfigBean;
	}

	private SystemConfigBean() {

	}

	/**
	 * Method invoke during server up time to determine server start up
	 * mode and based on mode it will pick up corresponding profile.
	 * mode[3]={'Build','Production','Test'};
	 * Profile[3]={'Startup-Mode-BUID.xml','Startup-Mode-PRODUCTION.xml','Startup-Mode-TEST.xml'};
	 */
	private void initStartupMode() {
		InputStream inputStream = null;
		String startupFileName = getAttribute(STARTUP_MODE_FILE);
		if (StringUtils.hasText(startupFileName)) {
			File startupFile = new File(startupFileName);
			if (startupFile.isFile() && startupFile.canRead()) {
				try {
					inputStream = new FileInputStream(startupFile);
				} catch (FileNotFoundException e) {
					String err = "server start mode file is not defined in IIMSCfg.xml";
					System.err.println(err);
				}
			}
		}
		if (inputStream == null) {
			String startupMode = getAttribute(STARTUP_MODE);
			if (!StringUtils.hasText(startupMode)) {
				startupMode = DEFAULT_STARTUP_MODE;
			}

			String classPathResourceLocation = MessageFormat.format(
					CFG_STARTUP_MODE_LOCATION, startupMode.toUpperCase());
			ClassPathResource cpr = new ClassPathResource(
					classPathResourceLocation);
			try {
				inputStream = cpr.getInputStream();
			} catch (IOException e) {
				String err = "Error occured while getting input stream from classPathResource"
						+ cpr;
				System.out.println(err);
				e.printStackTrace();
				throw new RuntimeException(err, e);
			}
		}
		if (inputStream == null) {
			System.err.println("Input stream is not found inside classPathResource");
			return;
		}
		try {
			startupModeDoc = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(inputStream);
		} catch (Exception ex) {
			String err = "Error occured while parsing Input stream or creating Document";
			logger.fatal(err);
			ex.printStackTrace();
			throw new RuntimeException(err, ex);
		}
		
		/*
		 * Write Probe
		 */
		try {

			String dirPath = getAttribute("//Config/EnvSystem/DataRoot") + File.separatorChar + "ConfigXMLProbe";
			File dir = new File(dirPath);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String filePath = dirPath + File.separatorChar + "MergedXML.xml";
			File file = new File(filePath);
			if (!file.exists()) {
				file.createNewFile();
			}
			
			StreamResult sr = new StreamResult(filePath);

			Transformer tf = TransformerFactory.newInstance().newTransformer();
			tf.setOutputProperty(OutputKeys.INDENT, "yes");
			tf.transform(new DOMSource(systemConfigXmlDoc), sr);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}



	public static String getAttribute(String key)
			throws SystemConfigurationException {
		// Added by Alok, CR# - COM#03220
		if (systemConfigMap.containsKey(key)) {
			if (systemConfigMap.get(key) instanceof String) {
				return (String) systemConfigMap.get(key);
			}
		}
		try {
		
					XPathFactory xfac = XPathFactory.newInstance();
					XPath xp = xfac.newXPath();
				
					String node = (String) xp.evaluate(key, systemConfigXmlDoc,
							XPathConstants.STRING);
					// Added by Alok, CR# - COM#03220
					systemConfigMap.put(key, node);
					return node;
		} catch (Exception ex) {
					//XPathExpressionException ex
					throw new SystemConfigurationException(ex);
		}		
		
	}
	

	public static synchronized String getAttributeFromNode(String key, Node node)
			throws SystemConfigurationException {
		XPathFactory xfac = XPathFactory.newInstance();
		XPath xp = xfac.newXPath();
		try {
			String nodeValue = (String) xp.evaluate(key, node,
					XPathConstants.STRING);
			return nodeValue;
		} catch (XPathExpressionException ex) {
			throw new SystemConfigurationException(ex);
		}
	}

	public static Node getNode(String key) throws SystemConfigurationException {
		XPathFactory xfac = XPathFactory.newInstance();
		XPath xp = xfac.newXPath();
		try {
			Node node = (Node) xp.evaluate(key, systemConfigXmlDoc,
					XPathConstants.NODE);
			return node;
		} catch (XPathExpressionException ex) {
			throw new SystemConfigurationException(ex);
		}
	}

	public static NodeList getNodeList(String key)
			throws SystemConfigurationException {
		XPathFactory xfac = XPathFactory.newInstance();
		XPath xp = xfac.newXPath();
		try {
			NodeList nodeList = (NodeList) xp.evaluate(key, systemConfigXmlDoc,
					XPathConstants.NODESET);
			return nodeList;
		} catch (XPathExpressionException ex) {
			throw new SystemConfigurationException(ex);
		}
	}

	public static Boolean isMapBasedCacheEnabled()
	{
		return isMapBasedCacheEnabled;
	}
	
	public static List<String> getAttributeValueList(String key)
	throws SystemConfigurationException {
		List<String> valueList = new ArrayList<String>();
		NodeList nodeList = getNodeList(key);
		if(nodeList != null) {
			for(int i=0;i < nodeList.getLength();i++) {
				valueList.add(nodeList.item(i).getNodeValue());
			}
		}
		return valueList;
	}
	
	public static void setAttribute(String key, String value)
			throws SystemConfigurationException {
		XPathFactory xfac = XPathFactory.newInstance();
		XPath xp = xfac.newXPath();
		try {
			Node node = (Node) xp.evaluate(key, systemConfigXmlDoc,
					XPathConstants.NODE);
			// node.setNodeValue(value);
			node.setTextContent(value);
			systemConfigMap.put(key, value);
		} catch (XPathExpressionException ex) {
			throw new SystemConfigurationException(ex);
		}
	}

	public static synchronized String[] getAttributeList(String key)
			throws SystemConfigurationException {
		// Added by Alok, CR# - COM#03220
		if (systemConfigMap.containsKey(key)) {
			if (systemConfigMap.get(key) instanceof String[]) {
				return (String[]) systemConfigMap.get(key);
			}
		}
		NodeList nodelist = getNodeList(key);
		String[] attributeList = new String[nodelist.getLength()];
		for (int i = 0; i < nodelist.getLength(); i++) {
			attributeList[i] = nodelist.item(i).getFirstChild().getNodeValue();
		}
		// Added by Alok, CR# - COM#03220
		systemConfigMap.put(key, attributeList);
		return attributeList;
	}

	
	/**
	 * 
	 * @param beanName
	 * @return
	 */
	public boolean doLazyLoad(String beanName) {
		assert StringUtils.hasText(beanName) : "BeanName cannot be empty or null";
		return Boolean.parseBoolean(getAttributeFromNode(MessageFormat.format(
				LAZY_LOAD_BEAN, beanName), startupModeDoc));
	}
	
	public String getXpathQueryForSystemNode(String instanceName) {
		return "";
	}

	public ApplicationContext getApplicationContext() {
		return this.applicationContext;
	}

	public void setApplicationContext(ApplicationContext applicationContext) {
		if (this.applicationContext == null) {
			this.applicationContext = applicationContext;
		}

	}

	public static Document getSystemConfigXmlDoc() {
		return systemConfigXmlDoc;
	}

	public static void setSystemConfigXmlDoc(Document systemConfigXmlDoc) {
		SystemConfigBean.systemConfigXmlDoc = systemConfigXmlDoc;
	}

	public static String getUrlPrefix() {
		if (urlPrf != null)
			return urlPrf;
		else {
			String urlPrefix = getAttribute("ENV_URL_PREFIX_KEY");
			urlPrf = urlPrefix;

			return urlPrf;
		}
	}

	public static String getEnvname() {
		String envName = getAttribute("ENV_NAME_KEY");
		if (envName != null) {
			return envName;
		}
		return "";
	}
	
	public static String getDefaultSite() {
		String envName = getAttribute("//Config/DefaultSite");
		if (envName != null) {
			return envName;
		}
		return "C01";
	}

	public static String getUrlPrf() {
		return urlPrf;
	}

	public static void setUrlPrf(String urlPrf) {
		SystemConfigBean.urlPrf = urlPrf;
	}
	
	public static String getOnsiteVersion(){
		String onsiteVersion = getAttribute("ONSITE_VERSION_KEY");
		if (onsiteVersion != null){
			return onsiteVersion;
		}
		return "";
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isMultiDBDeployment() {
		return YES_INDICATOR_CODE.equals(getAttribute("MULTI_DB_DEPLOYMENT_KEY"));
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isMultiDBConCurrentAccessAllowed() {
		
		if(isMultiDBDeployment())
		{
			return YES_INDICATOR_CODE.equals(getAttribute("MULTI_DB_CONCURRENT_ACCESS_KEY"));
		}
		return false;
	}
	public static String getInstallationName(){
		String jspTitle = getAttribute(JSPTITLE_CHANGE);
		return jspTitle;
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean isMultiLingualEnabled(){
		if(systemConfigMap == null){
			return false;
		}	
		return YES_INDICATOR_CODE.equalsIgnoreCase((getAttribute("MULTI_LINGUAL_KEY")));
	}
	
	/**
	 * 
	 * @return
	 */
	public static boolean doRenderButtonText(){
		if(systemConfigMap == null){
			return false;
		}	
		return YES_INDICATOR_CODE.equalsIgnoreCase((getAttribute("RENDER_BTN_TEXT_KEY")));
	}
	
	/**
	 * 
	 * @return
	 */
	public static NodeList getLanguageNodeList(){
		return getNodeList(LANGUAGE_LIST);
	}
	
	public static boolean isProspectivePriceEnabled(String siteCode)
	{
		String prospectivePriceValue = SystemConfigBean.getAttribute(new MessageFormat(PROSPECTIVE_PRICE_FILESYSTEM_BEAN_KEY).format(new String[]{siteCode}));
		
		if("Y".equalsIgnoreCase(prospectivePriceValue))
		{
			return true;
		}
		
		return false;
	}
	public static String getSystemUser(String tenantCode)
	{
		
		String systemUser = SystemConfigBean.getAttribute(new MessageFormat(MULTICURRENCY_ENABLED_FILESYSTEM_BEAN_KEY).format(new String[]{tenantCode}));
		
		
		return systemUser;
	}
	
	public static boolean isFundStatusMatrixApplicable(String siteCode)
	{
		String fundStatusMatrixApplValue = SystemConfigBean.getAttribute(new MessageFormat(FUND_STATUS_MATRIX_APPL_FILESYSTEM_BEAN_KEY).format(new String[]{siteCode}));
		
		if("Y".equalsIgnoreCase(fundStatusMatrixApplValue))
		{
			return true;
		}
		return false;
	}
	public static boolean isMultiCurrencyEnabled(String tenantCode)
	{
		//Need to check whether the tenant is available
		String multiCurrencyEnabled = SystemConfigBean.getAttribute(new MessageFormat(MULTICURRENCY_ENABLED_FILESYSTEM_BEAN_KEY).format(new String[]{tenantCode}));
		
		if("Y".equalsIgnoreCase(multiCurrencyEnabled))
		{
			return true;
		}
		return false;
	}
	
	public static String getDefaultRepositoryId(String tenantCode)
	{
		//Need to check whether the tenant is available
		String repositoryId = SystemConfigBean.getAttribute(MessageFormat.format(DOC_ENV_BAT_CM_REPO_ID_DEFAULT,tenantCode));
		
		
		return repositoryId;
	}
	
	public static String getDefaultRepositoryType(String tenantCode)
	{
		//Need to check whether the tenant is available
		String repositoryType = SystemConfigBean.getAttribute(MessageFormat.format(DOC_ENV_BAT_CM_REPO_TYPE_DEFAULT,tenantCode));
		
		
		return repositoryType;
	}
	
	public static String getDefaultDocumentType(String tenantCode)
	{
		//Need to check whether the tenant is available
		String docType = SystemConfigBean.getAttribute(MessageFormat.format(DOC_ENV_BAT_CM_DOC_TYPE_DEFAULT,tenantCode));
		
		
		return docType;
	}
	
	public static File getExecutable(String executable) {

		File executableFile = null;

		File file = new File(executable);
		if (file.isFile()) {

			executableFile = file;
		} else {

			String FilePath = SystemConfigBean.getAttribute(MessageFormat.format(EXECUTABLE_PATH, executable));
			executableFile = new File(FilePath);
		}

		return executableFile;
	}
	
}
