package com.lessons.weatherappandroid.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lessons.nasa.model.Weather;
import com.lessons.weatherappandroid.MainActivityKt;
import com.lessons.weatherappandroid.databinding.MainFragmentBinding;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Locale;

import kotlin.Lazy;
import retrofit2.HttpException;

import static org.koin.java.KoinJavaComponent.inject;

public class MainFragmentJava extends Fragment {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());

    private Lazy<MainViewModel> viewModel = inject(MainViewModel.class);
    private MainFragmentBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = MainFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel.getValue().getLiveDataWeather().observe(getViewLifecycleOwner(), weatherData -> renderData(weatherData));
        viewModel.getValue().getData("94043");
        binding.buttonSearch.setOnClickListener(view1 -> viewModel.getValue().getData(binding.zipCode.getText().toString()));
    }

    private void renderData(WeatherData data) {
        if (data.getClass().toString().equals(WeatherData.Success.class.toString())) {
            Weather serverResponseData = ((WeatherData.Success) data).getServerResponseData();
            binding.textLocation.setText(serverResponseData.getLocation());
            if (serverResponseData.getMain() != null) {
                binding.textTemperature.setText(serverResponseData.getMain().getTemp());
                binding.textHumidity.setText(serverResponseData.getMain().getHumidity() + "%");
            }
            if (serverResponseData.getWind() != null)
                binding.textWindSpeed.setText(serverResponseData.getWind().getSpeed());

            binding.textVisibilityy.setText(serverResponseData.getVisibility());
            if (serverResponseData.getSys() != null) {
                if (serverResponseData.getSys().getSunrise() != null)
                    binding.textSunrise.setText(dateFormat.format(serverResponseData.getSys().getSunrise() * 1000));
                if (serverResponseData.getSys().getSunset() != null)
                    binding.textSunset.setText(dateFormat.format(serverResponseData.getSys().getSunset() * 1000));
            }
            binding.progressBar.setVisibility(View.GONE);
        } else if (data.getClass().toString().equals(WeatherData.Loading.class.toString())) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else if (data.getClass().toString().equals(WeatherData.Error.class.toString())) {
            WeatherData.Error dataError = (WeatherData.Error) data;
            String errMes;
            try {
                if (dataError.getError() instanceof HttpException) {
                    String er = ((HttpException) dataError.getError()).response().errorBody().string();
                    if (er != null)
                        errMes = new JSONObject(er).getString("message");
                    else
                        errMes = dataError.getError().getMessage();
                } else
                    errMes = dataError.getError().getMessage();
            } catch (Exception e) {
                errMes = dataError.getError().getMessage();
            }
            MainActivityKt.snackBarError(getView(), errMes != null ? errMes : "");
            binding.progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
