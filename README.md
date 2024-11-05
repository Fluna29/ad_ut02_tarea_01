# AD. 24/25. UT02. Tarea 01 - Manejo de ficheros

## UT01. Introducción al acceso a datos - Tarea 01 - Repaso unidad

### Introducción
Mediante la realización de esta tarea vamos a repasar algunos de los conceptos vistos en esta unidad.

### Tareas

#### 1. Se acerca el invierno… y los exámenes

1.1. En el repositorio de GitHub *game-of-thrones*, mantenido por Jeffrey Lancaster, podemos encontrar un conjunto de datos y visualizaciones de Juego de Tronos. Hay un artículo sobre él en Medium.

1.2. Entre la inmensa cantidad de datos existente, nos vamos a centrar en el JSON con información sobre los personajes: **characters.json**.

1.3. En esta [ubicación](https://raw.githubusercontent.com/jeffreylancaster/game-of-thrones/refs/heads/master/data/characters.json) puedes encontrar información sobre su estructura. Ten en cuenta que al trabajar con archivos JSON no disponemos de un esquema (xsd o similar) que nos ayude a la hora de parsear o validar los datos como lo tenemos cuando trabajamos con XML.

#### 2. Crear proyecto

2.1. Utilizando la última versión de IntelliJ Idea crea un nuevo proyecto de Java que se llame `ad_ut02_tarea_01`. Selecciona **Build System = IntelliJ** y **JDK = openjdk-23**.

2.2. Ejecútalo para ver que funciona correctamente.

#### 3. Control de versiones

3.1. Habilita el control de versiones.
- VCS > Enable Version Control Integration… > Como sistema de control de versiones selecciona **Git** > OK.

3.2. Echa un vistazo al archivo `.gitignore` para que te hagas una idea de todas las carpetas y archivos que no se van a añadir al control de versiones.

3.3. Muestra la ventana de herramientas para hacer un commit.
- **View > Tools Windows > Commit**.

3.4. Una vez abierta la ventana anterior, haz que se vean los cambios pendientes agrupados por directorio y módulo. También haz que se muestren los archivos ignorados (que no se tendrán en cuenta en el control de versiones).
- **View options (icono del ojo) > Group By > Directory Group By > Module Show > Ignored Files**.

3.5. Haz el primer commit:
- 3.5.1. Selecciona todos los archivos no versionados (unos 7 aprox.).
- 3.5.2. Pon un mensaje descriptivo para el commit (“Commit inicial”, “Proyecto recién creado”, …).
- 3.5.3. Haz el commit pulsando en el botón **Commit**.

3.6. Muestra la ventana de herramientas de **Git** y observa el commit que acabas de hacer y sus archivos.
- **View > Tools Windows > Git**.

3.7. Sube el proyecto a GitHub (crear un proyecto en GitHub y hacer un push).
- **Git > Git Hub > Share Project On GitHub** > marca el proyecto como privado (**Private**) > **Share**.

3.8. Comprueba que en la ventana de **Git** de Android Studio se ha añadido una ubicación remota.

3.9. Comprueba en la web de GitHub que se ha subido tu proyecto y que es privado:
- `https://github.com/<USUARIO>/ad_ut02_tarea_01`.

#### 4. Leer JSON

4.1. Añade la librería **Gson** a tu proyecto.

4.2. Crea la función `leerJSON`:
- Que reciba por parámetro un `String` que represente una URL.
- Tiene que convertir su contenido en objetos haciendo uso de la librería **Gson**.

4.3. Llámala desde `main` con la URL de GitHub de **characters.json**.

4.4. Crea las clases necesarias y sus atributos para transformar el contenido del JSON en objetos. No intentes mapear `houseName`. A veces viene como un `String` y otras como un array de `Strings`.

4.5. Haz una prueba de funcionamiento.

4.6. Haz commit y push con todos los cambios.

4.7. Fuera de concurso: haz que `houseName` se pueda cargar sin problemas.

#### 5. JAXB - Marshalling

5.1. Añade las librerías necesarias para que tu proyecto pueda usar **Jakarta XML Binding**.

5.2. Modifica la función `leerJSON` para que, en vez de devolver `void`, devuelva el objeto resultado.

5.3. Modifica tus clases con las anotaciones para poder hacer marshalling. Requisitos:
- Todas las etiquetas en el XML deben seguir la notación snake case, aunque el nombre de su atributo correspondiente en la clase sea camel case.
- Ejemplo: `private String characterName;` => `<character_name>Daenerys Targaryen</character_name>`.

5.4. En tu proyecto, crea una carpeta llamada `pruebas`.

5.5. Crea la función `marshalling`:
- Que reciba por parámetro un objeto y la ruta del fichero XML a generar.
- Tiene que convertir los objetos en XML.

5.6. Llámala desde `main` tras `leerJSON` con el resultado de `leerJSON`. El resultado se debe guardar en el fichero `characters.xml` en la ruta `pruebas`.

5.7. Haz una prueba de funcionamiento.

5.8. Haz commit y push con todos los cambios. No te olvides de añadir al control de versiones el fichero `characters.xml` que se haya generado.

#### 6. JAXP - Validación

6.1. Utilizando las herramientas de IntelliJ, genera un esquema `.xsd` a partir del fichero `characters.xml` y guárdalo en el directorio `pruebas`.

6.2. Crea la función `validateXml`:
- Que reciba por parámetro la ruta de un XML y la ruta de un esquema.
- Tiene que comprobar la validez de un XML contra el esquema XSD.
- Debe hacer uso del **JAXP Validation API**.
- Y mostrar por consola:
    - `Documento <fulanito> válido / Documento <fulanito> no válido: Cannot find …`

6.3. Llámala desde `main` tras `marshalling` para que compruebe la validez de `characters.xml` contra el esquema `characters.xsd`.

6.4. Haz una copia de `characters.xml` y llámala `characters_novalid.xml`. A unos de los elementos `<character>` quítale un elemento (y solo uno) que sea obligatorio.

6.5. Llama a `validateXml` en el `main` para comprobar la no validez de `characters_novalid.xml` contra `characters.xsd`.

6.6. Haz una prueba de funcionamiento.

6.7. Haz commit y push con todos los cambios. No te olvides de añadir al control de versiones el fichero `characters.xsd` y `characters_novalid.xml`.

#### 7. JAXP - Parsing API basado en DOM

7.1. Crea la función `parsingDom`:
- Que reciba por parámetro la ruta de un XML.
- Debe parsear el XML en documento DOM y devolver una lista de objetos personaje.
- Tiene que recorrer todos los elementos `character` y, para cada uno de ellos:
    - Crear un objeto personaje y añadirlo a la lista. No es necesario obtener y rellenar todos los atributos de la clase del personaje, solamente aquellos que no sean listas:
        - `actorLink`, `actorName`, `characterImageFull`, …
    - Mostrar, por consola, el nombre del personaje y del actor/actriz que lo interpreta:
        - `Personaje: Addam Marbrand. Actor: B.J. Hogg.`
        - `Personaje: Aegon Targaryen. Actor: ?.`

7.2. Llámala desde `main` tras `validateXml`. Pásale la ruta del fichero `characters.xml`.

7.3. Haz una prueba de funcionamiento.

7.4. Haz commit y push con todos los cambios.

#### 8. Streams basados en caracteres

8.1. Crea la función `generateCsv`:
- Que reciba por parámetro una lista de personajes y la ruta de un archivo donde guardarla.
- Debe generar un archivo CSV (pero separado por `;`).
- Deben exportarse todos los atributos de los personajes que no sean listas:
    - `actorLink`, `actorName`, `characterImageFull`, …

- La primera línea del archivo debe ser para los nombres de los atributos con la primera letra en mayúscula y con un espacio en blanco entre palabras:
    - `characterImageThumb` => `Character Image Thumb`.

8.2. Llámala desde `main` tras `parsingDom`. Pásale la lista de personajes devueltos por `parsingDom`. El archivo destino tiene que llamarse `characters.csv` y guardarse en el directorio `pruebas`.

8.3. Haz una prueba de funcionamiento. Abre el archivo generado con alguna herramienta que permita leer hojas de cálculo.

8.4. Haz commit y push con todos los cambios.

### Entrega

- Comparte tu proyecto con el profesor (ajprofessor) para que lo pueda consultar.
- La URL de tu proyecto en GitHub.
