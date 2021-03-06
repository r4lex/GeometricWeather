package wangdaye.com.geometricweather.data.entity.model.weather;

import android.content.Context;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.entity.result.accu.AccuHourlyResult;
import wangdaye.com.geometricweather.data.entity.result.caiyun.CaiYunMainlyResult;
import wangdaye.com.geometricweather.data.entity.result.cn.CNWeatherResult;
import wangdaye.com.geometricweather.data.entity.table.weather.HourlyEntity;
import wangdaye.com.geometricweather.utils.helpter.WeatherHelper;
import wangdaye.com.geometricweather.utils.manager.TimeManager;

/**
 * Hourly.
 * */

public class Hourly {

    public String time;
    public boolean dayTime;
    public String weather;
    public String weatherKind;
    public int temp;
    public int precipitation;

    public Hourly() {}

    public Hourly buildHourly(Context c, AccuHourlyResult result) {
        time = buildTime(c, result.DateTime.split("T")[1].split(":")[0]);
        dayTime = result.IsDaylight;
        weather = result.IconPhrase;
        weatherKind = WeatherHelper.getAccuWeatherKind(result.WeatherIcon);
        temp = (int) result.Temperature.Value;
        precipitation = result.PrecipitationProbability;
        return this;
    }

    public Hourly buildHourly(Context c,
                              CNWeatherResult.WeatherX today, CNWeatherResult.HourlyForecast hourly) {
        time = buildTime(c, hourly.hour);
        dayTime = TimeManager.isDaylight(hourly.hour, today.info.day.get(5), today.info.night.get(5));
        weather = hourly.info;
        weatherKind = WeatherHelper.getCNWeatherKind(hourly.img);
        temp = Integer.parseInt(hourly.temperature);
        precipitation = -1;
        return this;
    }

    public Hourly buildHourly(Context c, CaiYunMainlyResult result, int index) {
        int hour = Integer.parseInt(result.forecastHourly.temperature.pubTime.split("T")[1].substring(0, 2));
        hour = (hour + index) % 24;
        time = buildTime(c, String.valueOf(hour));
        dayTime = TimeManager.isDaylight(
                String.valueOf(hour),
                result.forecastDaily.sunRiseSet.value.get(0).from.split("T")[1].substring(0, 5),
                result.forecastDaily.sunRiseSet.value.get(0).to.split("T")[1].substring(0, 5));
        weather = WeatherHelper.getCNWeatherName(String.valueOf(result.forecastHourly.weather.value.get(index)));
        weatherKind = WeatherHelper.getCNWeatherKind(String.valueOf(result.forecastHourly.weather.value.get(index)));
        temp = result.forecastHourly.temperature.value.get(index);
        precipitation = -1;
        return this;
    }

    Hourly buildHourly(HourlyEntity entity) {
        time = entity.time;
        dayTime = entity.dayTime;
        weather = entity.weather;
        weatherKind = entity.weatherKind;
        temp = entity.temp;
        precipitation = entity.precipitation;
        return this;
    }

    private String buildTime(Context c, String hourString) {
        if (TimeManager.is12Hour(c)) {
            try {
                int hour = Integer.parseInt(hourString);
                if (hour == 0) {
                    hour = 24;
                }
                if (hour > 12) {
                    hour -= 12;
                }
                return hour + c.getString(R.string.of_clock);
            } catch (Exception ignored) {
                // do nothing.
            }
        }
        return hourString + c.getString(R.string.of_clock);
    }

    private String buildTime(Context c, String hourString, int offset) {
        if (TimeManager.is12Hour(c)) {
            try {
                int hour = Integer.parseInt(hourString);
                if (hour == 0) {
                    hour = 24;
                }
                if (hour > 12) {
                    hour -= 12;
                }
                return hour + c.getString(R.string.of_clock);
            } catch (Exception ignored) {
                // do nothing.
            }
        }
        return hourString + c.getString(R.string.of_clock);
    }
}
