package wangdaye.com.geometricweather.ui.adapter;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import wangdaye.com.geometricweather.R;
import wangdaye.com.geometricweather.data.entity.model.weather.Weather;
import wangdaye.com.geometricweather.ui.widget.RoundProgress;
import wangdaye.com.geometricweather.utils.helpter.WeatherHelper;

/**
 * Aqi adapter.
 * */

public class AqiAdapter extends RecyclerView.Adapter<AqiAdapter.ViewHolder> {

    private List<AqiItem> itemList;
    private List<ViewHolder> holderList;

    private class AqiItem {
        @ColorInt int color;
        float progress;
        float max;
        String title;
        String content;

        boolean executeAnimation;

        AqiItem(@ColorInt int color, float progress, float max, String title, String content,
                boolean executeAnimation) {
            this.color = color;
            this.progress = progress;
            this.max = max;
            this.title = title;
            this.content = content;
            this.executeAnimation = executeAnimation;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @Nullable AqiItem item;
        private boolean executeAnimation;
        @Nullable private AnimatorSet attachAnimatorSet;

        private TextView title;
        private TextView content;
        private RoundProgress progress;

        ViewHolder(View itemView) {
            super(itemView);
            this.title = itemView.findViewById(R.id.item_aqi_title);
            this.content = itemView.findViewById(R.id.item_aqi_content);
            this.progress = itemView.findViewById(R.id.item_aqi_progress);
        }

        void onBindView(AqiItem item) {
            this.item = item;
            this.executeAnimation = item.executeAnimation;

            title.setText(item.title);
            content.setText(item.content);

            if (executeAnimation) {
                progress.setProgress(0);
                progress.setProgressColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.colorLevel_1));
                progress.setProgressBackgroundColor(
                        ContextCompat.getColor(itemView.getContext(), R.color.colorLine));
            } else {
                progress.setProgress((int) (100.0 * item.progress / item.max));
                progress.setProgressColor(item.color);
                progress.setProgressBackgroundColor(Color.argb(
                        (int) (255 * 0.1),
                        Color.red(item.color),
                        Color.green(item.color),
                        Color.blue(item.color)));
            }
        }

        void executeAnimation() {
            if (executeAnimation && item != null) {
                executeAnimation = false;

                ValueAnimator progressColor = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ContextCompat.getColor(itemView.getContext(), R.color.colorLevel_1),
                        item.color);
                progressColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progress.setProgressColor((Integer) animation.getAnimatedValue());
                    }
                });

                ValueAnimator backgroundColor = ValueAnimator.ofObject(new ArgbEvaluator(),
                        ContextCompat.getColor(itemView.getContext(), R.color.colorLine),
                        Color.argb(
                                (int) (255 * 0.1),
                                Color.red(item.color),
                                Color.green(item.color),
                                Color.blue(item.color)));
                backgroundColor.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progress.setProgressBackgroundColor((Integer) animation.getAnimatedValue());
                    }
                });

                ValueAnimator aqiNumber = ValueAnimator.ofObject(new FloatEvaluator(), 0, item.progress);
                aqiNumber.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        progress.setProgress(
                                (int) (100.0 * ((Float) animation.getAnimatedValue()) / item.max));
                    }
                });

                attachAnimatorSet = new AnimatorSet();
                attachAnimatorSet.playTogether(progressColor, backgroundColor, aqiNumber);
                attachAnimatorSet.setInterpolator(new DecelerateInterpolator());
                attachAnimatorSet.setDuration((long) (item.progress / item.max * 5000));
                attachAnimatorSet.start();
            }
        }

        void cancelAnimation() {
            if (attachAnimatorSet != null && attachAnimatorSet.isRunning()) {
                attachAnimatorSet.cancel();
            }
            attachAnimatorSet = null;
        }
    }

    public AqiAdapter(Context context, Weather weather, boolean executeAnimation) {
        this.itemList = new ArrayList<>();
        if (weather != null && weather.aqi != null) {
            if (weather.aqi.pm25 >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getPm25Color(context, weather.aqi.pm25),
                        weather.aqi.pm25,
                        250,
                        "PM2.5",
                        weather.aqi.pm25 + " μg/m³",
                        executeAnimation));
            }
            if (weather.aqi.pm10 >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getPm10Color(context, weather.aqi.pm10),
                        weather.aqi.pm10,
                        420,
                        "PM10",
                        weather.aqi.pm10 + " μg/m³",
                        executeAnimation));
            }
            if (weather.aqi.so2 >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getSo2Color(context, weather.aqi.so2),
                        weather.aqi.so2,
                        1600,
                        "SO2",
                        weather.aqi.so2 + " μg/m³",
                        executeAnimation));
            }
            if (weather.aqi.no2 >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getNo2Color(context, weather.aqi.no2),
                        weather.aqi.no2,
                        565,
                        "NO2",
                        weather.aqi.no2 + " μg/m³",
                        executeAnimation));
            }
            if (weather.aqi.o3 >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getO3Color(context, weather.aqi.o3),
                        weather.aqi.o3,
                        800,
                        "O3",
                        weather.aqi.o3 + " μg/m³",
                        executeAnimation));
            }
            if (weather.aqi.co >= 0) {
                itemList.add(new AqiItem(
                        WeatherHelper.getCOColor(context, weather.aqi.co),
                        weather.aqi.co,
                        90,
                        "CO",
                        weather.aqi.co + " μg/m³",
                        executeAnimation));
            }
        }

        this.holderList = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_aqi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.onBindView(itemList.get(position));
        if (itemList.get(position).executeAnimation) {
            holderList.add(holder);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void executeAnimation() {
        for (int i = 0; i < holderList.size(); i ++) {
            holderList.get(i).executeAnimation();
        }
    }

    public void cancelAnimation() {
        for (int i = 0; i < holderList.size(); i ++) {
            holderList.get(i).cancelAnimation();
        }
        holderList.clear();
    }
}
