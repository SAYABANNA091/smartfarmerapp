package com.soni.services.web.models

import com.google.gson.annotations.SerializedName


data class WeekForecast(

    @SerializedName("Headline") var Headline: Headline? = Headline(),
    @SerializedName("DailyForecasts") var DailyForecasts: ArrayList<DailyForecasts> = arrayListOf()

)

data class Headline(

    @SerializedName("EffectiveDate") var EffectiveDate: String? = null,
    @SerializedName("EffectiveEpochDate") var EffectiveEpochDate: String? = null,
    @SerializedName("Severity") var Severity: String? = null,
    @SerializedName("Text") var Text: String? = null,
    @SerializedName("Category") var Category: String? = null,
    @SerializedName("EndDate") var EndDate: String? = null,
    @SerializedName("EndEpochDate") var EndEpochDate: String? = null,
    @SerializedName("MobileLink") var MobileLink: String? = null,
    @SerializedName("Link") var Link: String? = null

)

data class Day(

    @SerializedName("Icon") var Icon: String? = null,
    @SerializedName("IconPhrase") var IconPhrase: String? = null,
    @SerializedName("HasPrecipitation") var HasPrecipitation: Boolean? = null,
    @SerializedName("ShortPhrase") var ShortPhrase: String? = null,
    @SerializedName("LongPhrase") var LongPhrase: String? = null,
    @SerializedName("PrecipitationProbability") var PrecipitationProbability: String? = null,
    @SerializedName("ThunderstormProbability") var ThunderstormProbability: String? = null,
    @SerializedName("RainProbability") var RainProbability: String? = null,
    @SerializedName("SnowProbability") var SnowProbability: String? = null,
    @SerializedName("IceProbability") var IceProbability: String? = null,
    @SerializedName("Wind") var Wind: Wind? = Wind(),
    @SerializedName("WindGust") var WindGust: WindGust? = WindGust(),
    @SerializedName("TotalLiquid") var TotalLiquid: TotalLiquid? = TotalLiquid(),
    @SerializedName("Rain") var Rain: Rain? = Rain(),
    @SerializedName("Snow") var Snow: Snow? = Snow(),
    @SerializedName("Ice") var Ice: Ice? = Ice(),
    @SerializedName("HoursOfPrecipitation") var HoursOfPrecipitation: String? = null,
    @SerializedName("HoursOfRain") var HoursOfRain: String? = null,
    @SerializedName("HoursOfSnow") var HoursOfSnow: String? = null,
    @SerializedName("HoursOfIce") var HoursOfIce: String? = null,
    @SerializedName("CloudCover") var CloudCover: String? = null,
    @SerializedName("Evapotranspiration") var Evapotranspiration: Evapotranspiration? = Evapotranspiration(),
    @SerializedName("SolarIrradiance") var SolarIrradiance: SolarIrradiance? = SolarIrradiance()

)

data class Night(

    @SerializedName("Icon") var Icon: String? = null,
    @SerializedName("IconPhrase") var IconPhrase: String? = null,
    @SerializedName("HasPrecipitation") var HasPrecipitation: Boolean? = null,
    @SerializedName("ShortPhrase") var ShortPhrase: String? = null,
    @SerializedName("LongPhrase") var LongPhrase: String? = null,
    @SerializedName("PrecipitationProbability") var PrecipitationProbability: String? = null,
    @SerializedName("ThunderstormProbability") var ThunderstormProbability: String? = null,
    @SerializedName("RainProbability") var RainProbability: String? = null,
    @SerializedName("SnowProbability") var SnowProbability: String? = null,
    @SerializedName("IceProbability") var IceProbability: String? = null,
    @SerializedName("Wind") var Wind: Wind? = Wind(),
    @SerializedName("WindGust") var WindGust: WindGust? = WindGust(),
    @SerializedName("TotalLiquid") var TotalLiquid: TotalLiquid? = TotalLiquid(),
    @SerializedName("Rain") var Rain: Rain? = Rain(),
    @SerializedName("Snow") var Snow: Snow? = Snow(),
    @SerializedName("Ice") var Ice: Ice? = Ice(),
    @SerializedName("HoursOfPrecipitation") var HoursOfPrecipitation: String? = null,
    @SerializedName("HoursOfRain") var HoursOfRain: String? = null,
    @SerializedName("HoursOfSnow") var HoursOfSnow: String? = null,
    @SerializedName("HoursOfIce") var HoursOfIce: String? = null,
    @SerializedName("CloudCover") var CloudCover: String? = null,
    @SerializedName("Evapotranspiration") var Evapotranspiration: Evapotranspiration? = Evapotranspiration(),
    @SerializedName("SolarIrradiance") var SolarIrradiance: SolarIrradiance? = SolarIrradiance()

)

data class DailyForecasts(

    @SerializedName("Date") var Date: String? = null,
    @SerializedName("EpochDate") var EpochDate: String? = null,
    @SerializedName("Sun") var Sun: Sun? = Sun(),
    @SerializedName("Moon") var Moon: Moon? = Moon(),
    @SerializedName("Temperature") var Temperature: TemperatureWeek? = TemperatureWeek(),
    @SerializedName("RealFeelTemperature") var RealFeelTemperature: TemperatureWeek? = TemperatureWeek(),
    @SerializedName("RealFeelTemperatureShade") var RealFeelTemperatureShade: TemperatureWeek? = TemperatureWeek(),
    @SerializedName("HoursOfSun") var HoursOfSun: Double? = null,
    @SerializedName("DegreeDaySummary") var DegreeDaySummary: DegreeDaySummary? = DegreeDaySummary(),
    @SerializedName("AirAndPollen") var AirAndPollen: ArrayList<AirAndPollen> = arrayListOf(),
    @SerializedName("Day") var Day: Day? = Day(),
    @SerializedName("Night") var Night: Night? = Night(),
    @SerializedName("Sources") var Sources: ArrayList<String> = arrayListOf(),
    @SerializedName("MobileLink") var MobileLink: String? = null,
    @SerializedName("Link") var Link: String? = null

)

data class Sun(

    @SerializedName("Rise") var Rise: String? = null,
    @SerializedName("EpochRise") var EpochRise: String? = null,
    @SerializedName("Set") var Set: String? = null,
    @SerializedName("EpochSet") var EpochSet: String? = null

)
data class TemperatureWeek (

    @SerializedName("Minimum" ) var Minimum : Minimum? = Minimum(),
    @SerializedName("Maximum" ) var Maximum : Maximum? = Maximum()

)
data class Moon(

    @SerializedName("Rise") var Rise: String? = null,
    @SerializedName("EpochRise") var EpochRise: String? = null,
    @SerializedName("Set") var Set: String? = null,
    @SerializedName("EpochSet") var EpochSet: String? = null,
    @SerializedName("Phase") var Phase: String? = null,
    @SerializedName("Age") var Age: String? = null

)

data class Minimum(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Maximum(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)


data class Heating(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Cooling(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class DegreeDaySummary(

    @SerializedName("Heating") var Heating: Heating? = Heating(),
    @SerializedName("Cooling") var Cooling: Cooling? = Cooling()

)

data class AirAndPollen(

    @SerializedName("Name") var Name: String? = null,
    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Category") var Category: String? = null,
    @SerializedName("CategoryValue") var CategoryValue: String? = null,
    @SerializedName("Type") var Type: String? = null

)