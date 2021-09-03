# Cucumber
UNLaM - Sistemas Operativos Avanzados - 2900 (2C2020)

## Cucumber
Es una app mobile desarrollada para Android que se comunica con un servidor a través de una API implementando Retrofit. Usa Firebase para el manejo de datos de la aplicación, hace uso de diferentes sensores del dispositivo móvil y de Shared Preferences para la persistencia de datos.

La funcionalidad de la aplicación radica en proporcionar información relevante acerca del cultivo de plantas para poder ayudar a que sus usuarios puedan construir su propio huerto urbano.
Principalmente, ofrece una descripción general de las plantas en cuestión, ciertos cuidados a tener en cuenta, la frecuencia de riego recomendable y el tamaño de la maceta o contenedor necesario para el cultivo de las mismas.
También brinda información respecto a los meses recomendables para el cultivo y la cosecha, ya que es necesario saber los tiempos que requiere cada una de estas plantas para ello, además de los cuidados relacionados con el clima.


## HPC
La sección de HPC consta de 3 ejercicios prácticos, de los cuales los dos primeros implementan CUDA y el tercero OpenCL.
La idea de estos ejercicios es comparar las ejecuciones tanto en CPU como en GPU, ejecuciones secuenciales y paralelas, y la toma de los tiempos de ejecución en ambos entornos.

### Ejercicio 1:
Este ejercicio consiste en la realización de operaciones entre vectores (es decir, una dimensión). Realiza la función "rot" (rotación de puntos en el plano) proporcionada por las rutinas y funciones del nivel 1 de BLAS.

### Ejercicio 2:
Este ejercicio consiste en espejar una imagen respecto a su eje x (dos dimensiones). Las imágenes son tratadas como matrices de píxeles, siendo cada posición x e y de la matriz un píxel completo, para este caso, con sus 3 componentes RGB.
Para este caso, el espejado de las imágenes fue implementado con las bases de la transposición de matrices, con la salvedad que solamente se transpone sobre el eje x para que tenga el efecto de espejado de manera horizontal.

### Ejercicio 3:
Este ejercicio se encarga de aplicar diferentes filtros a una imagen dada. Los filtros ofrecidos son: complemento, escala de grises, sepia, umbral, Sobel y contraste.
