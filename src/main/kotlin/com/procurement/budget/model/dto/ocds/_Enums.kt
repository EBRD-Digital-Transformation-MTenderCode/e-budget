package com.procurement.budget.model.dto.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.budget.exception.EnumException
import java.util.*


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