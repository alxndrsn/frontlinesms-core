/**
 * 
 */
package net.frontlinesms.ui.handler;

import java.util.Arrays;
import java.util.Map.Entry;

import net.frontlinesms.AppProperties;
import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.Utils;
import net.frontlinesms.data.StatisticsManager;
import net.frontlinesms.email.EmailException;
import net.frontlinesms.email.smtp.SmtpEmailSender;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.i18n.TextResourceKeyOwner;

import org.apache.log4j.Logger;

/**
 * @author Morgan Belkadi <morgan@frontlinesms.com>
 */
@TextResourceKeyOwner
public class StatisticsDialogHandler implements ThinletUiEventHandler {


//> UI LAYOUT FILES
	public static final String UI_FILE_STATISTICS_FORM = "/ui/core/statistics/dgStatistics.xml";
	private static final String COMPONENT_EMAIL_TEXTFIELD = "tfEmail";
	
//> UI COMPONENT NAMES
	private static final String COMPONENT_TA_STATS_CONTENT = "statsContent";
	private static final String COMPONENT_SECTOR_LIST = "cbSector";
	private static final String COMPONENT_COUNTRY_LIST = "cbCountry";

	private static final String I18N_STATS_DIALOG_THANKS = "stats.dialog.thanks";

//> INSTANCE PROPERTIES
	/** Logger */
	private Logger LOG = Utils.getLogger(this.getClass());
	
	private UiGeneratorController ui;
	/** Manager of statistics */
	private StatisticsManager statisticsManager;
	
	private Object dialogComponent;
	
	public StatisticsDialogHandler(UiGeneratorController ui) {
		this.ui = ui;
		this.statisticsManager = ui.getFrontlineController().getStatisticsManager();
	}
	
	/**
	 * Initialize the statistics dialog
	 */
	private void initDialog() {
		LOG.trace("INIT STATISTICS DIALOG");
		this.dialogComponent = ui.loadComponentFromFile(UI_FILE_STATISTICS_FORM, this);
		
		// Initialise the country list
		initCountryList(AppProperties.getInstance().getWorkCountry());
		// set value for email if it's currently saved
		ui.setText(find(COMPONENT_EMAIL_TEXTFIELD), AppProperties.getInstance().getUserEmail());
		// set value for sector if it's currently saved
		setSelectedSector(AppProperties.getInstance().getWorkSector());
		
		this.statisticsManager.collectData();
		
		// Log the stats data.
		LOG.info(statisticsManager.getDataAsEmailString());

		Object taStatsContent = ui.find(dialogComponent, COMPONENT_TA_STATS_CONTENT);
		for (Entry<String, String> entry : this.statisticsManager.getStatisticsList().entrySet()) {
			ui.add(taStatsContent, getRow(entry));
		}
		this.saveLastPromptDate();

		LOG.trace("EXIT");
	}
	
	/**
	 * Set the selected sector in the sector list.
	 * @param sector
	 */
	private void setSelectedSector(String sector) {
		Object sectorList = find(COMPONENT_SECTOR_LIST);
		int currentIndex = 0;
		for(Object sectorComponent : ui.getItems(sectorList)) {
			String text = ui.getText(sectorComponent);
			if(text.equals(sector)) {
				ui.setSelectedIndex(sectorList, currentIndex);
				break;
			}
			++currentIndex;
		}
	}
	
	/**
	 * Initialise the country list.
	 * @param countryCode The code of the country to select initially.
	 */
	private void initCountryList(String countryCode) {
		Object countryList = find(COMPONENT_COUNTRY_LIST);
		int currentIndex = ui.getItems(countryList).length;
		for(StatCountry c : StatCountry.values()) {
			Object countryChoice = ui.createComboboxChoice(c.getEnglishName(), c);
			ui.setIcon(countryChoice, "/icons/flags/" + c.getCode() + ".png");
			ui.add(countryList, countryChoice);
			if(c.getCode().equals(countryCode)) {
				ui.setSelectedIndex(countryList, currentIndex);
			}
			++currentIndex;
		}
	}
	
	/**
	 * Creates a Thinlet UI table row containing details of a statistics key/value element.
	 * @param key
	 * @param value
	 * @return
	 */
	public Object getRow(Entry<String, String> entry) {
		String key = entry.getKey();
		Object row = ui.createTableRow(key);
		
		String label;
		if(StatisticsManager.isCompositeKey(key)) {
			String[] parts = StatisticsManager.splitStatsMapKey(key);
			if(parts.length > 1) {
				String[] subsequentParts = Arrays.copyOfRange(parts, 1, parts.length);
				label = InternationalisationUtils.getI18NString(parts[0], subsequentParts);
			} else label = InternationalisationUtils.getI18NString(key);
		} else label = InternationalisationUtils.getI18NString(key);
		ui.add(row, ui.createTableCell(label));
		ui.add(row, ui.createTableCell(entry.getValue()));
		
		return row;
	}
	
	/**
	 * The dialog being shown, properties must be updated
	 */
	private void saveLastPromptDate() {
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsPromptDate();
		appProperties.saveToDisk();
	}

	/**
	 * The statistics being sent, properties must be updated
	 */
	private void saveLastSubmissionDate() {
		// We save the current state of the number of messages
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setLastStatisticsSubmissionDate();
		appProperties.saveToDisk();
	}
	
	/**
	 * @return the instance of the statistics dialog 
	 */
	public Object getDialog() {
		initDialog();
		
		return this.dialogComponent;
	}

	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		this.ui.removeDialog(dialogComponent);
	}
//> UI EVENT METHODS
	
	/**
	 * This method is called when the YES button is pressed in the statistics dialog.
	 */
	public void sendStatistics() {
		// Gather the email, country and sector values
		String userEmail = getUserEmail();
		int sectorIndex = getSectorIndex();
		String countryCode = getCountryCode();
		
		this.statisticsManager.setUserEmailAddress(userEmail);
		this.statisticsManager.setWorkSector(sectorIndex);
		this.statisticsManager.setCountry(countryCode);
		
		// TODO save values for email, sector and country
		AppProperties appProperties = AppProperties.getInstance();
		appProperties.setUserEmail(userEmail);
		appProperties.setWorkSector(getSectorName());
		appProperties.setWorkCountry(countryCode);
		appProperties.saveToDisk();
		
		if (!sendStatisticsViaEmail()) {
			sendStatisticsViaSms();
		}
		
		this.saveLastSubmissionDate();
		
		this.ui.alert(InternationalisationUtils.getI18NString(I18N_STATS_DIALOG_THANKS));
		this.removeDialog();
	}
	
	private String getUserEmail() {
		return ui.getText(find(COMPONENT_EMAIL_TEXTFIELD));
	}
	private int getSectorIndex() {
		return ui.getSelectedIndex(find(COMPONENT_SECTOR_LIST));
	}
	private String getSectorName() {
		return ui.getText(ui.getSelectedItem(find(COMPONENT_SECTOR_LIST)));
	}
	private String getCountryCode() {
		Object countryItem = ui.getSelectedItem(ui.find(COMPONENT_COUNTRY_LIST));
		StatCountry country = ui.getAttachedObject(countryItem, StatCountry.class);
		return country == null ? "" : country.getCode();
	}
	
	/**
	 * Actually send an SMS containing the statistics in a short version
	 */
	private void sendStatisticsViaSms() {
		String content = this.statisticsManager.getDataAsSmsString();
		String number = FrontlineSMSConstants.FRONTLINE_STATS_PHONE_NUMBER;
		this.ui.getFrontlineController().sendTextMessage(number, content);
	}
	
	/**
	 * Try to send an e-mail containing the statistics in plain text
	 * @return true if the statistics were successfully sent
	 */
	private boolean sendStatisticsViaEmail() {
		try {
			new SmtpEmailSender(FrontlineSMSConstants.FRONTLINE_SUPPORT_EMAIL_SERVER).sendEmail(
					FrontlineSMSConstants.FRONTLINE_STATS_EMAIL,
					this.statisticsManager.getUserEmailAddress(),
					"FrontlineSMS Statistics",
					getStatisticsForEmail());
			return true;
		} catch(EmailException ex) { 
			LOG.info("Sending statistics by email failed.", ex);
			return false;
		}
	}

	/**
	 * Gets the statistics in a format suitable for emailing.
	 * @param bob {@link StringBuilder} used for compiling the body of the e-mail.
	 */
	private String getStatisticsForEmail() {
		StringBuilder bob = new StringBuilder();
		beginSection(bob, "Statistics");
	    bob.append(this.statisticsManager.getDataAsEmailString());
		endSection(bob, "Statistics");
	    return bob.toString();
	}
	
	/** @return UI component with the supplied name, or <code>null</code> if none could be found */
	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}
	
	/**
	 * Starts a section of the e-mail's body.
	 * Sections started with this method should be ended with {@link #endSection(StringBuilder, String)}
	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
	 * @param sectionName The name of the section of the report that is being started.
	 */
	private static void beginSection(StringBuilder bob, String sectionName) {
		bob.append("\n### Begin Section '" + sectionName + "' ###\n");
	}
	
	/**
	 * Ends a section of the e-mail's body.
	 * Sections ended with this should have been started with {@link #beginSection(StringBuilder, String)}
	 * @param bob The {@link StringBuilder} used for building the e-mail's body.
	 * @param sectionName The name of the section of the report that is being started.
	 */
	private static void endSection(StringBuilder bob, String sectionName) {
		bob.append("### End Section '" + sectionName + "' ###\n");
	}
}

/** Countries selectable in the stats menu */
enum StatCountry {
	AFGHANISTAN("af","Afghanistan"),
	ALAND_ISLANDS("ax","Åland Islands"),
	ALBANIA("al","Albania"),
	ALGERIA("dz","Algeria"),
	AMERICAN_SAMOA("as","American Samoa"),
	ANDORRA("ad","Andorra"),
	ANGOLA("ao","Angola"),
	ANGUILLA("ai","Anguilla"),
	ANTARCTICA("aq","Antarctica"),
	ANTIGUA_AND_BARBUDA("ag","Antigua And Barbuda"),
	ARGENTINA("ar","Argentina"),
	ARMENIA("am","Armenia"),
	ARUBA("aw","Aruba"),
	AUSTRALIA("au","Australia"),
	AUSTRIA("at","Austria"),
	AZERBAIJAN("az","Azerbaijan"),
	BAHAMAS("bs","Bahamas"),
	BAHRAIN("bh","Bahrain"),
	BANGLADESH("bd","Bangladesh"),
	BARBADOS("bb","Barbados"),
	BELARUS("by","Belarus"),
	BELGIUM("be","Belgium"),
	BELIZE("bz","Belize"),
	BENIN("bj","Benin"),
	BERMUDA("bm","Bermuda"),
	BHUTAN("bt","Bhutan"),
	BOLIVIA("bo","Bolivia"),
	BOSNIA_AND_HERZEGOVINA("ba","Bosnia And Herzegovina"),
	BOTSWANA("bw","Botswana"),
	BOUVET_ISLAND("bv","Bouvet Island"),
	BRAZIL("br","Brazil"),
	BRITISH_INDIAN_OCEAN_TERRITORY("io","British Indian Ocean Territory"),
	BRUNEI_DARUSSALAM("bn","Brunei Darussalam"),
	BULGARIA("bg","Bulgaria"),
	BURKINA_FASO("bf","Burkina Faso"),
	BURUNDI("bi","Burundi"),
	CAMBODIA("kh","Cambodia"),
	CAMEROON("cm","Cameroon"),
	CANADA("ca","Canada"),
	CAPE_VERDE("cv","Cape Verde"),
	CAYMAN_ISLANDS("ky","Cayman Islands"),
	CENTRAL_AFRICAN_REPUBLIC("cf","Central African Republic"),
	CHAD("td","Chad"),
	CHILE("cl","Chile"),
	CHINA("cn","China"),
	CHRISTMAS_ISLAND("cx","Christmas Island"),
	COCOS_ISLANDS("cc","Cocos (Keeling) Islands"),
	COLOMBIA("co","Colombia"),
	COMOROS("km","Comoros"),
	CONGO("cg","Congo"),
	CONGO__THE_DEMOCRATIC_REPUBLIC_OF_THE("cd","Congo, The Democratic Republic of The"),
	COOK_ISLANDS("ck","Cook Islands"),
	COSTA_RICA("cr","Costa Rica"),
	COTE_DIVOIRE("ci","Cote D'Ivoire"),
	CROATIA("hr","Croatia"),
	CUBA("cu","Cuba"),
	CYPRUS("cy","Cyprus"),
	CZECH_REPUBLIC("cz","Czech Republic"),
	DENMARK("dk","Denmark"),
	DJIBOUTI("dj","Djibouti"),
	DOMINICA("dm","Dominica"),
	DOMINICAN_REPUBLIC("do","Dominican Republic"),
	ECUADOR("ec","Ecuador"),
	EGYPT("eg","Egypt"),
	EL_SALVADOR("sv","El Salvador"),
	EQUATORIAL_GUINEA("gq","Equatorial Guinea"),
	ERITREA("er","Eritrea"),
	ESTONIA("ee","Estonia"),
	ETHIOPIA("et","Ethiopia"),
	FALKLAND_ISLANDS("fk","Falkland Islands"),
	FAROE_ISLANDS("fo","Faroe Islands"),
	FIJI("fj","Fiji"),
	FINLAND("fi","Finland"),
	FRANCE("fr","France"),
	FRENCH_GUIANA("gf","French Guiana"),
	FRENCH_POLYNESIA("pf","French Polynesia"),
	FRENCH_SOUTHERN_TERRITORIES("tf","French Southern Territories"),
	GABON("ga","Gabon"),
	GAMBIA("gm","Gambia"),
	GEORGIA("ge","Georgia"),
	GERMANY("de","Germany"),
	GHANA("gh","Ghana"),
	GIBRALTAR("gi","Gibraltar"),
	GREECE("gr","Greece"),
	GREENLAND("gl","Greenland"),
	GRENADA("gd","Grenada"),
	GUADELOUPE("gp","Guadeloupe"),
	GUAM("gu","Guam"),
	GUATEMALA("gt","Guatemala"),
	GUERNSEY("G","Guernsey"),
	GUINEA("gn","Guinea"),
	GUINEA_BISSAU("gw","Guinea-Bissau"),
	GUYANA("gy","Guyana"),
	HAITI("ht","Haiti"),
	HEARD_ISLAND_AND_MCDONALD_ISLANDS("hm","Heard Island And Mcdonald Islands"),
	HOLY_SEE__VATICAN_CITY_STATE("va","Holy See (Vatican City State)"),
	HONDURAS("hn","Honduras"),
	HONG_KONG("hk","Hong Kong"),
	HUNGARY("hu","Hungary"),
	ICELAND("is","Iceland"),
	INDIA("in","India"),
	INDONESIA("id","Indonesia"),
	IRAN__ISLAMIC_REPUBLIC_OF("ir","Iran, Islamic Republic of"),
	IRAQ("iq","Iraq"),
	IRELAND("ie","Ireland"),
	ISLE_OF_MAN("im","Isle of Man"),
	ISRAEL("il","Israel"),
	ITALY("it","Italy"),
	JAMAICA("jm","Jamaica"),
	JAPAN("jp","Japan"),
	JERSEY("je","Jersey"),
	JORDAN("jo","Jordan"),
	KAZAKHSTAN("kz","Kazakhstan"),
	KENYA("ke","Kenya"),
	KIRIBATI("ki","Kiribati"),
	KOREA__DEMOCRATIC_PEOPLES_REPUBLIC_OF("kp","Korea, Democratic People's Republic of"),
	KOREA__REPUBLIC_OF("kr","Korea, Republic of"),
	KUWAIT("kw","Kuwait"),
	KYRGYZSTAN("kg","Kyrgyzstan"),
	LAO_PEOPLES_DEMOCRATIC_REPUBLIC("la","Lao People's Democratic Republic"),
	LATVIA("lv","Latvia"),
	LEBANON("lb","Lebanon"),
	LESOTHO("ls","Lesotho"),
	LIBERIA("lr","Liberia"),
	LIBYAN_ARAB_JAMAHIRIYA("ly","Libyan Arab Jamahiriya"),
	LIECHTENSTEIN("li","Liechtenstein"),
	LITHUANIA("lt","Lithuania"),
	LUXEMBOURG("lu","Luxembourg"),
	MACAO("mo","Macao"),
	MACEDONIA__THE_FORMER_YUGOSLAV_REPUBLIC_OF("mk","Macedonia, The former Yugoslav Republic of"),
	MADAGASCAR("mg","Madagascar"),
	MALAWI("mw","Malawi"),
	MALAYSIA("my","Malaysia"),
	MALDIVES("mv","Maldives"),
	MALI("ml","Mali"),
	MALTA("mt","Malta"),
	MARSHALL_ISLANDS("mh","Marshall Islands"),
	MARTINIQUE("mq","Martinique"),
	MAURITANIA("mr","Mauritania"),
	MAURITIUS("mu","Mauritius"),
	MAYOTTE("yt","Mayotte"),
	MEXICO("mx","Mexico"),
	MICRONESIA__FEDERATED_STATES_OF("fm","Micronesia, Federated States of"),
	MOLDOVA__REPUBLIC_OF("md","Moldova, Republic of"),
	MONACO("mc","Monaco"),
	MONGOLIA("mn","Mongolia"),
	MONTSERRAT("ms","Montserrat"),
	MOROCCO("ma","Morocco"),
	MOZAMBIQUE("mz","Mozambique"),
	MYANMAR("mm","Myanmar"),
	NAMIBIA("na","Namibia"),
	NAURU("nr","Nauru"),
	NEPAL("np","Nepal"),
	NETHERLANDS("nl","Netherlands"),
	NETHERLANDS_ANTILLES("an","Netherlands Antilles"),
	NEW_CALEDONIA("nc","New Caledonia"),
	NEW_ZEALAND("nz","New Zealand"),
	NICARAGUA("ni","Nicaragua"),
	NIGER("ne","Niger"),
	NIGERIA("ng","Nigeria"),
	NIUE("nu","Niue"),
	NORFOLK_ISLAND("nf","Norfolk Island"),
	NORTHERN_MARIANA_ISLANDS("mp","Northern Mariana Islands"),
	NORWAY("no","Norway"),
	OMAN("om","Oman"),
	PAKISTAN("pk","Pakistan"),
	PALAU("pw","Palau"),
	PALESTINIAN_TERRITORY__OCCUPIED("ps","Palestinian Territory, Occupied"),
	PANAMA("pa","Panama"),
	PAPUA_NEW_GUINEA("pg","Papua New Guinea"),
	PARAGUAY("py","Paraguay"),
	PERU("pe","Peru"),
	PHILIPPINES("ph","Philippines"),
	PITCAIRN("pn","Pitcairn"),
	POLAND("pl","Poland"),
	PORTUGAL("pt","Portugal"),
	PUERTO_RICO("pr","Puerto Rico"),
	QATAR("qa","Qatar"),
	REUNION("re","Reunion"),
	ROMANIA("ro","Romania"),
	RUSSIAN_FEDERATION("ru","Russian Federation"),
	RWANDA("rw","Rwanda"),
	SAINT_HELENA("sh","Saint Helena"),
	SAINT_KITTS_AND_NEVIS("kn","Saint Kitts And Nevis"),
	SAINT_LUCIA("lc","Saint Lucia"),
	SAINT_PIERRE_AND_MIQUELON("pm","Saint Pierre And Miquelon"),
	SAINT_VINCENT_AND_THE_GRENADINES("vc","Saint Vincent And The Grenadines"),
	SAMOA("ws","Samoa"),
	SAN_MARINO("sm","San Marino"),
	SAO_TOME_AND_PRINCIPE("st","Sao Tome And Principe"),
	SAUDI_ARABIA("sa","Saudi Arabia"),
	SENEGAL("sn","Senegal"),
	SERBIA_AND_MONTENEGRO("cs","Serbia And Montenegro"),
	SEYCHELLES("sc","Seychelles"),
	SIERRA_LEONE("sl","Sierra Leone"),
	SINGAPORE("sg","Singapore"),
	SLOVAKIA("sk","Slovakia"),
	SLOVENIA("si","Slovenia"),
	SOLOMON_ISLANDS("sb","Solomon Islands"),
	SOMALIA("so","Somalia"),
	SOUTH_AFRICA("za","South Africa"),
	SOUTH_GEORGIA_AND_THE_SOUTH_SANDWICH_ISLANDS("gs","South Georgia And The South Sandwich Islands"),
	SPAIN("es","Spain"),
	SRI_LANKA("lk","Sri Lanka"),
	SUDAN("sd","Sudan"),
	SURINAME("sr","Suriname"),
	SVALBARD_AND_JAN_MAYEN("sj","Svalbard And Jan Mayen"),
	SWAZILAND("sz","Swaziland"),
	SWEDEN("se","Sweden"),
	SWITZERLAND("ch","Switzerland"),
	SYRIAN_ARAB_REPUBLIC("sy","Syrian Arab Republic"),
	TAIWAN__PROVINCE_OF_CHINA("tw","Taiwan, Province of China"),
	TAJIKISTAN("tj","Tajikistan"),
	TANZANIA__UNITED_REPUBLIC_OF("tz","Tanzania, United Republic of"),
	THAILAND("th","Thailand"),
	TIMOR_LESTE("tl","Timor-Leste"),
	TOGO("tg","Togo"),
	TOKELAU("tk","Tokelau"),
	TONGA("to","Tonga"),
	TRINIDAD_AND_TOBAGO("tt","Trinidad And Tobago"),
	TUNISIA("tn","Tunisia"),
	TURKEY("tr","Turkey"),
	TURKMENISTAN("tm","Turkmenistan"),
	TURKS_AND_CAICOS_ISLANDS("tc","Turks And Caicos Islands"),
	TUVALU("tv","Tuvalu"),
	UGANDA("ug","Uganda"),
	UKRAINE("ua","Ukraine"),
	UNITED_ARAB_EMIRATES("ae","United Arab Emirates"),
	UNITED_KINGDOM("gb","United Kingdom"),
	UNITED_STATES("us","United States"),
	UNITED_STATES_MINOR_OUTLYING_ISLANDS("um","United States Minor Outlying Islands"),
	URUGUAY("uy","Uruguay"),
	UZBEKISTAN("uz","Uzbekistan"),
	VANUATU("vu","Vanuatu"),
	VENEZUELA("ve","Venezuela"),
	VIET_NAM("vn","Viet Nam"),
	VIRGIN_ISLANDS__BRITISH("vg","Virgin Islands, British"),
	VIRGIN_ISLANDS__US("vi","Virgin Islands, U.S."),
	WALLIS_AND_FUTUNA("wf","Wallis And Futuna"),
	WESTERN_SAHARA("eh","Western Sahara"),
	YEMEN("ye","Yemen"),
	ZAMBIA("zm","Zambia"),
	ZIMBABWE("zw","Zimbabwe");
	
	StatCountry(String code, String englishName) {
		this.code = code;
		this.englishName = englishName;
	}

	private final String code;
	private final String englishName;
	
	public String getCode() {
		return code;
	}
	
	public String getEnglishName() {
		return englishName;
	}
}