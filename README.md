# Weather app with Jetpack Compose
A sample application developed using Jetpack Compose
---
### Demo
<p>
<img src="/attachments/home_screen.gif" width="250" />
<img src="/attachments/detail_screen.gif" width="250" />
</p>

### Supported API (free)

- OpenWeatherMap
- Algolia Search

### Technique & libraries

#### Supported libraries

- Clean architecture
- Kotlin coroutines
- Room database
- Hilt dependency injection
- Retrofit

#### Experimental libraries

- Material 3
- AnimatedContent
- Accompanist permissions

### App description

- Search and display weather by city or from current location
- Cache up to 5 cities in Room DB and use it in case of no connection

### Unit test

- Kotlin coroutines test

### UI test

- Macro benchmark test

### Custom composable
- Multiple lines chart (for temperature, humidity, clouds, sea level, pressure,...)
- Bar chart (for rain)
