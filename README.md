Rappi Android Test
==================

Esta aplicación usa Android Architecture Components integrado con Dagger 2. Basada en el ejemplo
[GithubBrowserSample][github-sample]
brindado por Google.

Introdución
-----------
La aplicación consta en un buscandor de peliculas y series a traves de las categorías
(upcoming, top rated, popular) usando el api
### Funcionalmente
Esta aplicación cuenta con 4 fragments todos integrados dentro de una misma activity (MainActivity),
cuya navegación y/o interacción es llevada a través del componente de arquitectura de navegación
(Navigation Architecture Component). Por lo que este projecto es `single activity` como se
recomienda en [Android Deverlopers][navigation]


#### MovieSearchFragment & TvShowSearchFragment
Permite buscar peliculas por categorias (upcoming, top rated, popular)
Cada resultado de las busquedas es almacenado en la base de datos en la tabla `SearchResult` donde
la lista de los IDs (MovieId y TvShowTv) se llevan a una simple columna.

Cada vez que una nueva página de busqueda es obtenida, el mismo registro con la nueva lista de IDs.

#### MovieFragment & TvSearchFragment
En este fragment se muestra un simple detalle de las peliculas o series dependiendo sea el caso.

### Flujo
1. Al realizar una petición http que no se haya hecho anteriormente, el resultado es almacenado en
base de datos y posteriormente mostrado
en pantalla.
2. Al realizar una petición http que ya ha sido guardada previamente en base de datos, el resultado
previo es proveído directamente a la vista mientras el nuevo resultado es esperado desde la petición
hecha al API.

### Building
Puedes abrir el projecto en Android Studio versión 3.2 Canary 14 o mayor y compilarlo.
### Testing
Proximamente...

### Librerias
* [Android Support Library][support-lib]
* [Android Architecture Components][arch]
* [Android Data Binding][data-binding]
* [Dagger 2][dagger2] para inyeccción de dependencias
* [Retrofit][retrofit] para la comuniacion con el api REST
* [Glide][glide] para cargar imagenes
* [Timber][timber] para logging


[github-sample]: https://github.com/googlesamples/android-architecture-components/tree/master/GithubBrowserSample
[navigation]: https://developer.android.com/topic/libraries/architecture/navigation/
[support-lib]: https://developer.android.com/topic/libraries/support-library/index.html
[arch]: https://developer.android.com/arch
[data-binding]: https://developer.android.com/topic/libraries/data-binding/index.html
[espresso]: https://google.github.io/android-testing-support-library/docs/espresso/
[dagger2]: https://google.github.io/dagger
[retrofit]: http://square.github.io/retrofit
[glide]: https://github.com/bumptech/glide
[timber]: https://github.com/JakeWharton/timber