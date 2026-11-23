# Notas de revisión (21-nov-2026)

## Cambios
- Catálogo de recetas: el DAO consulta los ingredientes mediante `receta_ingrediente`; la pantalla ahora muestra el texto de ingredientes en varias líneas, permite leer celdas largas y hace más anchas las columnas para reducir truncamiento.
- Si una receta no tiene relaciones guardadas, se muestra `Sin ingredientes asociados` en lugar de una celda vacía.
- Dashboard de stock: `Próxima caducidad` se muestra como fecha del lote vigente con stock que vence primero. Si no hay stock, muestra `—`; si hay stock pero los lotes no tienen fecha de caducidad, muestra `Sin fecha`.
- Proyecto NetBeans configurado para Java 21, con el que se verificó compilación en este entorno.
- Se agregó `diagnostico_datos.sql` con SELECT de solo lectura para revisar recetas, relaciones, lotes, caducidad y conteos. No contiene TRUNCATE ni operaciones destructivas.

## Nota importante
La consulta original de receta ya utilizaba la tabla intermedia `receta_ingrediente`, por lo que no era necesario agregar un campo de ingredientes a `receta`. Si la base de datos local tiene recetas pero no sus relaciones, la pantalla no puede inventar ingredientes: ejecutar `diagnostico_datos.sql` en MySQL para comprobar el contenido real.

La base de datos local del usuario no fue accesible desde este entorno; por tanto, no se han ejecutado consultas contra ella ni se ha alterado ningún dato real.
