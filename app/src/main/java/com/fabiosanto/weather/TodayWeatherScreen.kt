package com.fabiosanto.weather

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayWeatherScreen(
    modifier: Modifier = Modifier,
    viewModel: TodayWeatherViewModel
) {

    val viewState by viewModel.viewState.collectAsStateWithLifecycle(
        initialValue = TodayWeatherViewState.Idle,
        lifecycle = LocalLifecycleOwner.current.lifecycle
    )

    Scaffold {
        Box(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val state = viewState) {
                TodayWeatherViewState.Idle -> CircularProgressIndicator()
                is TodayWeatherViewState.ShowContent ->
                    Column(
                        Modifier
                            .background(MaterialTheme.colorScheme.inversePrimary, RoundedCornerShape(20.dp))
                            .padding(28.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                modifier = Modifier.size(68.dp),
                                painter = painterResource(id = state.weatherIcon),
                                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary),
                                contentDescription = state.description
                            )

                            Text(
                                modifier = modifier,
                                text = state.temperature,
                                style = MaterialTheme.typography.headlineLarge.copy(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                        Text(
                            modifier = modifier,
                            text = state.description,
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
            }
        }
    }
}