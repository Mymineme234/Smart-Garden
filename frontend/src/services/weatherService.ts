export async function getCurrentWeather(lat: number, lon: number) {
  const API_KEY = import.meta.env.VITE_OPENWEATHER_KEY

  const res = await fetch(
    `https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&lang=vi&appid=${API_KEY}`
  )

  if (!res.ok) throw new Error("Failed to fetch weather")
  return res.json()
}
