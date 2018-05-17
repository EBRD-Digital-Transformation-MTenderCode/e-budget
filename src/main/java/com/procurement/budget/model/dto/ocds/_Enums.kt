package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.exception.EnumException
import java.util.*

enum class Currency constructor(private val value: String) {

    ADP("ADP"),
    AED("AED"),
    AFA("AFA"),
    AFN("AFN"),
    ALK("ALK"),
    ALL("ALL"),
    AMD("AMD"),
    ANG("ANG"),
    AOA("AOA"),
    AOK("AOK"),
    AON("AON"),
    AOR("AOR"),
    ARA("ARA"),
    ARP("ARP"),
    ARS("ARS"),
    ARY("ARY"),
    ATS("ATS"),
    AUD("AUD"),
    AWG("AWG"),
    AYM("AYM"),
    AZM("AZM"),
    AZN("AZN"),
    BAD("BAD"),
    BAM("BAM"),
    BBD("BBD"),
    BDT("BDT"),
    BEC("BEC"),
    BEF("BEF"),
    BEL("BEL"),
    BGJ("BGJ"),
    BGK("BGK"),
    BGL("BGL"),
    BGN("BGN"),
    BHD("BHD"),
    BIF("BIF"),
    BMD("BMD"),
    BND("BND"),
    BOB("BOB"),
    BOP("BOP"),
    BOV("BOV"),
    BRB("BRB"),
    BRC("BRC"),
    BRE("BRE"),
    BRL("BRL"),
    BRN("BRN"),
    BRR("BRR"),
    BSD("BSD"),
    BTN("BTN"),
    BUK("BUK"),
    BWP("BWP"),
    BYB("BYB"),
    BYN("BYN"),
    BYR("BYR"),
    BZD("BZD"),
    CAD("CAD"),
    CDF("CDF"),
    CHC("CHC"),
    CHE("CHE"),
    CHF("CHF"),
    CHW("CHW"),
    CLF("CLF"),
    CLP("CLP"),
    CNY("CNY"),
    COP("COP"),
    COU("COU"),
    CRC("CRC"),
    CSD("CSD"),
    CSJ("CSJ"),
    CSK("CSK"),
    CUC("CUC"),
    CUP("CUP"),
    CVE("CVE"),
    CYP("CYP"),
    CZK("CZK"),
    DDM("DDM"),
    DEM("DEM"),
    DJF("DJF"),
    DKK("DKK"),
    DOP("DOP"),
    DZD("DZD"),
    ECS("ECS"),
    ECV("ECV"),
    EEK("EEK"),
    EGP("EGP"),
    ERN("ERN"),
    ESA("ESA"),
    ESB("ESB"),
    ESP("ESP"),
    ETB("ETB"),
    EUR("EUR"),
    FIM("FIM"),
    FJD("FJD"),
    FKP("FKP"),
    FRF("FRF"),
    GBP("GBP"),
    GEK("GEK"),
    GEL("GEL"),
    GHC("GHC"),
    GHP("GHP"),
    GHS("GHS"),
    GIP("GIP"),
    GMD("GMD"),
    GNE("GNE"),
    GNF("GNF"),
    GNS("GNS"),
    GQE("GQE"),
    GRD("GRD"),
    GTQ("GTQ"),
    GWE("GWE"),
    GWP("GWP"),
    GYD("GYD"),
    HKD("HKD"),
    HNL("HNL"),
    HRD("HRD"),
    HRK("HRK"),
    HTG("HTG"),
    HUF("HUF"),
    IDR("IDR"),
    IEP("IEP"),
    ILP("ILP"),
    ILR("ILR"),
    ILS("ILS"),
    INR("INR"),
    IQD("IQD"),
    IRR("IRR"),
    ISJ("ISJ"),
    ISK("ISK"),
    ITL("ITL"),
    JMD("JMD"),
    JOD("JOD"),
    JPY("JPY"),
    KES("KES"),
    KGS("KGS"),
    KHR("KHR"),
    KMF("KMF"),
    KPW("KPW"),
    KRW("KRW"),
    KWD("KWD"),
    KYD("KYD"),
    KZT("KZT"),
    LAJ("LAJ"),
    LAK("LAK"),
    LBP("LBP"),
    LKR("LKR"),
    LRD("LRD"),
    LSL("LSL"),
    LSM("LSM"),
    LTL("LTL"),
    LTT("LTT"),
    LUC("LUC"),
    LUF("LUF"),
    LUL("LUL"),
    LVL("LVL"),
    LVR("LVR"),
    LYD("LYD"),
    MAD("MAD"),
    MDL("MDL"),
    MGA("MGA"),
    MGF("MGF"),
    MKD("MKD"),
    MLF("MLF"),
    MMK("MMK"),
    MNT("MNT"),
    MOP("MOP"),
    MRO("MRO"),
    MTL("MTL"),
    MTP("MTP"),
    MUR("MUR"),
    MVQ("MVQ"),
    MVR("MVR"),
    MWK("MWK"),
    MXN("MXN"),
    MXP("MXP"),
    MXV("MXV"),
    MYR("MYR"),
    MZE("MZE"),
    MZM("MZM"),
    MZN("MZN"),
    NAD("NAD"),
    NGN("NGN"),
    NIC("NIC"),
    NIO("NIO"),
    NLG("NLG"),
    NOK("NOK"),
    NPR("NPR"),
    NZD("NZD"),
    OMR("OMR"),
    PAB("PAB"),
    PEH("PEH"),
    PEI("PEI"),
    PEN("PEN"),
    PES("PES"),
    PGK("PGK"),
    PHP("PHP"),
    PKR("PKR"),
    PLN("PLN"),
    PLZ("PLZ"),
    PTE("PTE"),
    PYG("PYG"),
    QAR("QAR"),
    RHD("RHD"),
    ROK("ROK"),
    ROL("ROL"),
    RON("RON"),
    RSD("RSD"),
    RUB("RUB"),
    RUR("RUR"),
    RWF("RWF"),
    SAR("SAR"),
    SBD("SBD"),
    SCR("SCR"),
    SDD("SDD"),
    SDG("SDG"),
    SDP("SDP"),
    SEK("SEK"),
    SGD("SGD"),
    SHP("SHP"),
    SIT("SIT"),
    SKK("SKK"),
    SLL("SLL"),
    SOS("SOS"),
    SRD("SRD"),
    SRG("SRG"),
    SSP("SSP"),
    STD("STD"),
    SUR("SUR"),
    SVC("SVC"),
    SYP("SYP"),
    SZL("SZL"),
    THB("THB"),
    TJR("TJR"),
    TJS("TJS"),
    TMM("TMM"),
    TMT("TMT"),
    TND("TND"),
    TOP("TOP"),
    TPE("TPE"),
    TRL("TRL"),
    TRY("TRY"),
    TTD("TTD"),
    TWD("TWD"),
    TZS("TZS"),
    UAH("UAH"),
    UAK("UAK"),
    UGS("UGS"),
    UGW("UGW"),
    UGX("UGX"),
    USD("USD"),
    USN("USN"),
    USS("USS"),
    UYI("UYI"),
    UYN("UYN"),
    UYP("UYP"),
    UYU("UYU"),
    UZS("UZS"),
    VEB("VEB"),
    VEF("VEF"),
    VNC("VNC"),
    VND("VND"),
    VUV("VUV"),
    WST("WST"),
    XAF("XAF"),
    XAG("XAG"),
    XAU("XAU"),
    XBA("XBA"),
    XBB("XBB"),
    XBC("XBC"),
    XBD("XBD"),
    XCD("XCD"),
    XDR("XDR"),
    XEU("XEU"),
    XFO("XFO"),
    XFU("XFU"),
    XOF("XOF"),
    XPD("XPD"),
    XPF("XPF"),
    XPT("XPT"),
    XRE("XRE"),
    XSU("XSU"),
    XTS("XTS"),
    XUA("XUA"),
    XXX("XXX"),
    YDD("YDD"),
    YER("YER"),
    YUD("YUD"),
    YUM("YUM"),
    YUN("YUN"),
    ZAL("ZAL"),
    ZAR("ZAR"),
    ZMK("ZMK"),
    ZMW("ZMW"),
    ZRN("ZRN"),
    ZRZ("ZRZ"),
    ZWC("ZWC"),
    ZWD("ZWD"),
    ZWL("ZWL"),
    ZWN("ZWN"),
    ZWR("ZWR");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Currency>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Currency {
            return Currency.CONSTANTS[value]
                    ?: throw EnumException(Currency::class.java.name, value, Arrays.toString(values()))
        }
    }
}


enum class MainGeneralActivity constructor(private val value: String) {
    DEFENCE("DEFENCE"),
    ECONOMIC_AND_FINANCIAL_AFFAIRS("ECONOMIC_AND_FINANCIAL_AFFAIRS"),
    EDUCATION("EDUCATION"),
    ENVIRONMENT("ENVIRONMENT"),
    GENERAL_PUBLIC_SERVICES("GENERAL_PUBLIC_SERVICES"),
    HEALTH("HEALTH"),
    HOUSING_AND_COMMUNITY_AMENITIES("HOUSING_AND_COMMUNITY_AMENITIES"),
    PUBLIC_ORDER_AND_SAFETY("PUBLIC_ORDER_AND_SAFETY"),
    RECREATION_CULTURE_AND_RELIGION("RECREATION_CULTURE_AND_RELIGION"),
    SOCIAL_PROTECTION("SOCIAL_PROTECTION");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MainGeneralActivity>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MainGeneralActivity {
            return CONSTANTS[value]
                    ?: throw EnumException(MainGeneralActivity::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class MainSectoralActivity constructor(private val value: String) {
    AIRPORT_RELATED_ACTIVITIES("AIRPORT_RELATED_ACTIVITIES"),
    ELECTRICITY("ELECTRICITY"),
    EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL("EXPLORATION_EXTRACTION_COAL_OTHER_SOLID_FUEL"),
    EXPLORATION_EXTRACTION_GAS_OIL("EXPLORATION_EXTRACTION_GAS_OIL"),
    PORT_RELATED_ACTIVITIES("PORT_RELATED_ACTIVITIES"),
    POSTAL_SERVICES("POSTAL_SERVICES"),
    PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT("PRODUCTION_TRANSPORT_DISTRIBUTION_GAS_HEAT"),
    RAILWAY_SERVICES("RAILWAY_SERVICES"),
    URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES("URBAN_RAILWAY_TRAMWAY_TROLLEYBUS_BUS_SERVICES"),
    WATER("WATER");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, MainSectoralActivity>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): MainSectoralActivity {
            return CONSTANTS[value]
                    ?: throw EnumException(MainSectoralActivity::class.java.name, value, Arrays.toString(values()))
        }
    }


}

enum class Scale constructor(private val value: String) {
    MICRO("micro"),
    SME("sme"),
    LARGE("large"),
    EMPTY("");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, Scale>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Scale {
            return CONSTANTS[value] ?: throw EnumException(Scale::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class TypeOfBuyer constructor(private val value: String) {
    BODY_PUBLIC("BODY_PUBLIC"),
    EU_INSTITUTION("EU_INSTITUTION"),
    MINISTRY("MINISTRY"),
    NATIONAL_AGENCY("NATIONAL_AGENCY"),
    REGIONAL_AGENCY("REGIONAL_AGENCY"),
    REGIONAL_AUTHORITY("REGIONAL_AUTHORITY");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }

    companion object {
        private val CONSTANTS = HashMap<String, TypeOfBuyer>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TypeOfBuyer {
            return CONSTANTS[value]
                    ?: throw EnumException(TypeOfBuyer::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class Scheme constructor(private val value: String) {
    CPV("CPV"),
    CPVS("CPVS"),
    GSIN("GSIN"),
    UNSPSC("UNSPSC"),
    CPC("CPC"),
    OKDP("OKDP"),
    OKPD("OKPD");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, Scheme>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): Scheme {
            return CONSTANTS[value] ?: throw EnumException(Scheme::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class TenderStatus constructor(private val value: String) {
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete"),
    WITHDRAWN("withdrawn");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, TenderStatus>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TenderStatus {
            return CONSTANTS[value]
                    ?: throw EnumException(TenderStatus::class.java.name, value, Arrays.toString(values()))
        }
    }
}

enum class TenderStatusDetails constructor(private val value: String) {
    PRESELECTION("preselection"),
    PRESELECTED("preselected"),
    PREQUALIFICATION("prequalification"),
    PREQUALIFIED("prequalified"),
    EVALUATION("evaluation"),
    EVALUATED("evaluated"),
    EXECUTION("execution"),
    PLANNING("planning"),
    PLANNED("planned"),
    ACTIVE("active"),
    BLOCKED("blocked"),
    CANCELLED("cancelled"),
    UNSUCCESSFUL("unsuccessful"),
    COMPLETE("complete"),
    WITHDRAWN("withdrawn"),
    SUSPENDED("suspended"),
    EMPTY("empty");

    override fun toString(): String {
        return this.value
    }

    @JsonValue
    fun value(): String {
        return this.value
    }

    companion object {

        private val CONSTANTS = HashMap<String, TenderStatusDetails>()

        init {
            for (c in values()) {
                CONSTANTS[c.value] = c
            }
        }

        @JsonCreator
        fun fromValue(value: String): TenderStatusDetails {
            return CONSTANTS[value]
                    ?: throw EnumException(TenderStatusDetails::class.java.name, value, Arrays.toString(values()))
        }
    }
}