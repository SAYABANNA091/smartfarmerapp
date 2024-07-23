package com.soni.services.web.models

import com.google.gson.annotations.SerializedName

data class HourlyForecast(

    @SerializedName("DateTime") var DateTime: String? = null,
    @SerializedName("EpochDateTime") var EpochDateTime: String? = null,
    @SerializedName("WeatherIcon") var WeatherIcon: String? = null,
    @SerializedName("IconPhrase") var IconPhrase: String? = null,
    @SerializedName("HasPrecipitation") var HasPrecipitation: Boolean? = null,
    @SerializedName("IsDaylight") var IsDaylight: Boolean? = null,
    @SerializedName("Temperature") var Temperature: Temperature? = Temperature(),
    @SerializedName("RealFeelTemperature") var RealFeelTemperature: RealFeelTemperature? = RealFeelTemperature(),
    @SerializedName("RealFeelTemperatureShade") var RealFeelTemperatureShade: RealFeelTemperatureShade? = RealFeelTemperatureShade(),
    @SerializedName("WetBulbTemperature") var WetBulbTemperature: WetBulbTemperature? = WetBulbTemperature(),
    @SerializedName("DewPoString") var DewPoString: DewPoString? = DewPoString(),
    @SerializedName("Wind") var Wind: Wind? = Wind(),
    @SerializedName("WindGust") var WindGust: WindGust? = WindGust(),
    @SerializedName("RelativeHumidity") var RelativeHumidity: String? = null,
    @SerializedName("IndoorRelativeHumidity") var IndoorRelativeHumidity: String? = null,
    @SerializedName("Visibility") var Visibility: Visibility? = Visibility(),
    @SerializedName("Ceiling") var Ceiling: Ceiling? = Ceiling(),
    @SerializedName("UVIndex") var UVIndex: String? = null,
    @SerializedName("UVIndexText") var UVIndexText: String? = null,
    @SerializedName("PrecipitationProbability") var PrecipitationProbability: String? = null,
    @SerializedName("ThunderstormProbability") var ThunderstormProbability: String? = null,
    @SerializedName("RainProbability") var RainProbability: String? = null,
    @SerializedName("SnowProbability") var SnowProbability: String? = null,
    @SerializedName("IceProbability") var IceProbability: String? = null,
    @SerializedName("TotalLiquid") var TotalLiquid: TotalLiquid? = TotalLiquid(),
    @SerializedName("Rain") var Rain: Rain? = Rain(),
    @SerializedName("Snow") var Snow: Snow? = Snow(),
    @SerializedName("Ice") var Ice: Ice? = Ice(),
    @SerializedName("CloudCover") var CloudCover: String? = null,
    @SerializedName("Evapotranspiration") var Evapotranspiration: Evapotranspiration? = Evapotranspiration(),
    @SerializedName("SolarIrradiance") var SolarIrradiance: SolarIrradiance? = SolarIrradiance(),
    @SerializedName("MobileLink") var MobileLink: String? = null,
    @SerializedName("Link") var Link: String? = null

)


data class Temperature(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class RealFeelTemperature(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null,
    @SerializedName("Phrase") var Phrase: String? = null

)

data class RealFeelTemperatureShade(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null,
    @SerializedName("Phrase") var Phrase: String? = null

)

data class WetBulbTemperature(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class DewPoString(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Speed(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Direction(

    @SerializedName("Degrees") var Degrees: String? = null,
    @SerializedName("Localized") var Localized: String? = null,
    @SerializedName("English") var English: String? = null

)

data class Wind(

    @SerializedName("Speed") var Speed: Speed? = Speed(),
    @SerializedName("Direction") var Direction: Direction? = Direction()

)

data class WindGust(

    @SerializedName("Speed") var Speed: Speed? = Speed()

)

data class Visibility(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Ceiling(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class TotalLiquid(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Rain(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Snow(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Ice(

    @SerializedName("Value") var Value: String? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class Evapotranspiration(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)

data class SolarIrradiance(

    @SerializedName("Value") var Value: Double? = null,
    @SerializedName("Unit") var Unit: String? = null,
    @SerializedName("UnitType") var UnitType: String? = null

)
