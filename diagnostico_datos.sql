-- Diagnóstico de datos: consultas SOLO LECTURA. No modifica, borra ni reinicia información.
USE cocina_casera;

-- 1) Ingredientes que tiene registrada cada receta y sus cantidades/unidades.
SELECT r.id_receta, r.nombre AS receta, r.porciones_base,
       i.nombre AS ingrediente, ri.cantidad_requerida, u.abreviatura AS unidad
FROM receta r
LEFT JOIN receta_ingrediente ri ON ri.id_receta = r.id_receta
LEFT JOIN ingrediente i ON i.id_ingrediente = ri.id_ingrediente
LEFT JOIN unidad_medida u ON u.id_unidad = i.id_unidad
WHERE r.activa = 1
ORDER BY r.id_receta, i.nombre;

-- 2) Recetas sin ingredientes asociados (estas aparecerán como "Sin ingredientes asociados").
SELECT r.id_receta, r.nombre
FROM receta r
LEFT JOIN receta_ingrediente ri ON ri.id_receta = r.id_receta
WHERE r.activa = 1 AND ri.id_ingrediente IS NULL
ORDER BY r.nombre;

-- 3) Stock y fecha de caducidad próxima. Se consideran lotes con cantidad > 0
-- y que no hayan vencido; los lotes sin fecha se cuentan como disponibles.
SELECT i.id_ingrediente, i.nombre, u.abreviatura,
       COALESCE(SUM(CASE WHEN l.cantidad_actual > 0
                         AND (l.fecha_caducidad IS NULL OR l.fecha_caducidad >= CURRENT_DATE())
                    THEN l.cantidad_actual ELSE 0 END), 0) AS stock_disponible,
       MIN(CASE WHEN l.cantidad_actual > 0
                  AND (l.fecha_caducidad IS NULL OR l.fecha_caducidad >= CURRENT_DATE())
             THEN l.fecha_caducidad END) AS proxima_caducidad,
       COUNT(CASE WHEN l.cantidad_actual > 0
                     AND (l.fecha_caducidad IS NOT NULL AND l.fecha_caducidad < CURRENT_DATE())
                THEN 1 END) AS lotes_vencidos_con_stock
FROM ingrediente i
JOIN unidad_medida u ON u.id_unidad = i.id_unidad
LEFT JOIN inventario_lote l ON l.id_ingrediente = i.id_ingrediente
WHERE i.activo = 1
GROUP BY i.id_ingrediente, i.nombre, u.abreviatura
ORDER BY i.nombre;

-- 4) Conteo general para detectar si la carga de datos se quedó incompleta.
SELECT 'receta' AS tabla, COUNT(*) AS registros FROM receta
UNION ALL SELECT 'receta_ingrediente', COUNT(*) FROM receta_ingrediente
UNION ALL SELECT 'ingrediente', COUNT(*) FROM ingrediente
UNION ALL SELECT 'inventario_lote', COUNT(*) FROM inventario_lote;
