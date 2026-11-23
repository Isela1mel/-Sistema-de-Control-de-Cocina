-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-11-2026 a las 05:51:26
-- Versión del servidor: 10.4.28-MariaDB
-- Versión de PHP: 8.0.28

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `cocina_casera`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `detalle_transaccion`
--

CREATE TABLE `detalle_transaccion` (
  `id_detalle` bigint(20) UNSIGNED NOT NULL,
  `id_transaccion` bigint(20) UNSIGNED NOT NULL,
  `id_lote` bigint(20) UNSIGNED NOT NULL,
  `cantidad` decimal(12,3) NOT NULL
) ;

--
-- Volcado de datos para la tabla `detalle_transaccion`
--

INSERT INTO `detalle_transaccion` (`id_detalle`, `id_transaccion`, `id_lote`, `cantidad`) VALUES
(1, 1, 1, 200.000),
(2, 2, 2, 20.000),
(3, 3, 2, 2.000),
(4, 4, 1, 50.000),
(5, 4, 2, 1.000),
(6, 5, 1, 50.000),
(7, 5, 2, 1.000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `ingrediente`
--

CREATE TABLE `ingrediente` (
  `id_ingrediente` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `id_unidad` smallint(5) UNSIGNED NOT NULL,
  `dias_vida_util` smallint(5) UNSIGNED DEFAULT NULL,
  `origen` enum('CATALOGO','PERSONALIZADO') NOT NULL DEFAULT 'PERSONALIZADO',
  `notas` varchar(500) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `creado_en` datetime NOT NULL DEFAULT current_timestamp(),
  `actualizado_en` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ;

--
-- Volcado de datos para la tabla `ingrediente`
--

INSERT INTO `ingrediente` (`id_ingrediente`, `nombre`, `id_unidad`, `dias_vida_util`, `origen`, `notas`, `activo`, `creado_en`, `actualizado_en`) VALUES
(1, 'Yupi de cerdo', 1, 180, 'PERSONALIZADO', 'Refrigerar', 1, '2026-11-22 21:51:34', '2026-11-22 21:51:34'),
(2, 'Leche', 4, 180, 'PERSONALIZADO', 'Entera', 1, '2026-11-22 21:52:09', '2026-11-22 21:52:09');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `inventario_lote`
--

CREATE TABLE `inventario_lote` (
  `id_lote` bigint(20) UNSIGNED NOT NULL,
  `id_ingrediente` int(10) UNSIGNED NOT NULL,
  `cantidad_inicial` decimal(12,3) NOT NULL,
  `cantidad_actual` decimal(12,3) NOT NULL,
  `fecha_ingreso` datetime NOT NULL DEFAULT current_timestamp(),
  `fecha_caducidad` date DEFAULT NULL,
  `notas` varchar(500) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `inventario_lote`
--

INSERT INTO `inventario_lote` (`id_lote`, `id_ingrediente`, `cantidad_inicial`, `cantidad_actual`, `fecha_ingreso`, `fecha_caducidad`, `notas`) VALUES
(1, 1, 200.000, 100.000, '2026-11-22 21:51:34', '2027-05-21', 'Existencia inicial de ingrediente: Yupi de cerdo'),
(2, 2, 20.000, 16.000, '2026-11-22 21:52:09', '2027-05-21', 'Existencia inicial de ingrediente: Leche');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta`
--

CREATE TABLE `receta` (
  `id_receta` int(10) UNSIGNED NOT NULL,
  `nombre` varchar(150) NOT NULL,
  `imagen_url` varchar(500) DEFAULT NULL,
  `porciones_base` smallint(5) UNSIGNED NOT NULL DEFAULT 1,
  `instrucciones` text DEFAULT NULL,
  `activa` tinyint(1) NOT NULL DEFAULT 1,
  `creado_en` datetime NOT NULL DEFAULT current_timestamp(),
  `actualizado_en` datetime NOT NULL DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ;

--
-- Volcado de datos para la tabla `receta`
--

INSERT INTO `receta` (`id_receta`, `nombre`, `imagen_url`, `porciones_base`, `instrucciones`, `activa`, `creado_en`, `actualizado_en`) VALUES
(1, 'Yupi en dulce', NULL, 1, '1. Mezclar ingredientes.\n2. Cocinar en sartén.', 1, '2026-11-22 21:54:09', '2026-11-22 21:54:29');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `receta_ingrediente`
--

CREATE TABLE `receta_ingrediente` (
  `id_receta` int(10) UNSIGNED NOT NULL,
  `id_ingrediente` int(10) UNSIGNED NOT NULL,
  `cantidad_requerida` decimal(12,3) NOT NULL
) ;

--
-- Volcado de datos para la tabla `receta_ingrediente`
--

INSERT INTO `receta_ingrediente` (`id_receta`, `id_ingrediente`, `cantidad_requerida`) VALUES
(1, 1, 50.000),
(1, 2, 1.000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `transaccion`
--

CREATE TABLE `transaccion` (
  `id_transaccion` bigint(20) UNSIGNED NOT NULL,
  `tipo` enum('ENTRADA_COMPRA','MERMA','CONSUMO_RECETA') NOT NULL,
  `fecha_hora` datetime NOT NULL DEFAULT current_timestamp(),
  `id_usuario` int(10) UNSIGNED DEFAULT NULL,
  `id_receta` int(10) UNSIGNED DEFAULT NULL,
  `porciones_preparadas` smallint(5) UNSIGNED DEFAULT NULL,
  `descripcion` varchar(500) DEFAULT NULL
) ;

--
-- Volcado de datos para la tabla `transaccion`
--

INSERT INTO `transaccion` (`id_transaccion`, `tipo`, `fecha_hora`, `id_usuario`, `id_receta`, `porciones_preparadas`, `descripcion`) VALUES
(1, 'ENTRADA_COMPRA', '2026-11-22 21:51:34', NULL, NULL, NULL, 'Existencia inicial de ingrediente: Yupi de cerdo'),
(2, 'ENTRADA_COMPRA', '2026-11-22 21:52:09', NULL, NULL, NULL, 'Existencia inicial de ingrediente: Leche'),
(3, 'MERMA', '2026-11-22 21:55:23', NULL, NULL, NULL, 'leche agria'),
(4, 'CONSUMO_RECETA', '2026-11-22 21:58:36', NULL, 1, 1, 'Preparación de receta ID 1 (1 porciones)'),
(5, 'CONSUMO_RECETA', '2026-11-22 22:00:01', NULL, 1, 1, 'Preparación de receta ID 1 (1 porciones)');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `unidad_medida`
--

CREATE TABLE `unidad_medida` (
  `id_unidad` smallint(5) UNSIGNED NOT NULL,
  `nombre` varchar(40) NOT NULL,
  `abreviatura` varchar(10) NOT NULL,
  `dimension` enum('MASA','VOLUMEN','CONTEO') NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--
-- Volcado de datos para la tabla `unidad_medida`
--

INSERT INTO `unidad_medida` (`id_unidad`, `nombre`, `abreviatura`, `dimension`) VALUES
(1, 'Gramo', 'g', 'MASA'),
(2, 'Kilogramo', 'kg', 'MASA'),
(3, 'Mililitro', 'ml', 'VOLUMEN'),
(4, 'Litro', 'l', 'VOLUMEN'),
(5, 'Pieza', 'pz', 'CONTEO');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `id_usuario` int(10) UNSIGNED NOT NULL,
  `nombre_usuario` varchar(50) NOT NULL,
  `password_hash` varchar(255) NOT NULL,
  `nombre_completo` varchar(120) DEFAULT NULL,
  `activo` tinyint(1) NOT NULL DEFAULT 1,
  `creado_en` datetime NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_capacidad_recetas`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_capacidad_recetas` (
`id_receta` int(10) unsigned
,`receta` varchar(150)
,`porciones_base` smallint(5) unsigned
,`preparaciones_posibles` decimal(35,0)
,`porciones_posibles` decimal(40,0)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_historial_transacciones`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_historial_transacciones` (
`id_transaccion` bigint(20) unsigned
,`fecha_hora` datetime
,`tipo` enum('ENTRADA_COMPRA','MERMA','CONSUMO_RECETA')
,`receta_o_ingredientes` mediumtext
,`cantidad_total_movida` decimal(34,3)
,`porciones_preparadas` smallint(5) unsigned
,`descripcion` varchar(500)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_lotes_por_caducar`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_lotes_por_caducar` (
`id_lote` bigint(20) unsigned
,`id_ingrediente` int(10) unsigned
,`ingrediente` varchar(100)
,`cantidad_actual` decimal(12,3)
,`unidad` varchar(10)
,`fecha_caducidad` date
,`dias_restantes` int(7)
);

-- --------------------------------------------------------

--
-- Estructura Stand-in para la vista `v_stock_vigente`
-- (Véase abajo para la vista actual)
--
CREATE TABLE `v_stock_vigente` (
`id_ingrediente` int(10) unsigned
,`ingrediente` varchar(100)
,`unidad` varchar(40)
,`abreviatura` varchar(10)
,`existencia_vigente` decimal(34,3)
);

-- --------------------------------------------------------

--
-- Estructura para la vista `v_capacidad_recetas`
--
DROP TABLE IF EXISTS `v_capacidad_recetas`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_capacidad_recetas`  AS SELECT `r`.`id_receta` AS `id_receta`, `r`.`nombre` AS `receta`, `r`.`porciones_base` AS `porciones_base`, CASE WHEN count(`ri`.`id_ingrediente`) = 0 THEN 0 ELSE min(floor(coalesce((select sum(`l`.`cantidad_actual`) from `inventario_lote` `l` where `l`.`id_ingrediente` = `ri`.`id_ingrediente` and `l`.`cantidad_actual` > 0 and (`l`.`fecha_caducidad` is null or `l`.`fecha_caducidad` >= curdate())),0) / `ri`.`cantidad_requerida`)) END AS `preparaciones_posibles`, CASE WHEN count(`ri`.`id_ingrediente`) = 0 THEN 0 ELSE min(floor(coalesce((select sum(`l`.`cantidad_actual`) from `inventario_lote` `l` where `l`.`id_ingrediente` = `ri`.`id_ingrediente` and `l`.`cantidad_actual` > 0 and (`l`.`fecha_caducidad` is null or `l`.`fecha_caducidad` >= curdate())),0) / `ri`.`cantidad_requerida`)) * `r`.`porciones_base` END AS `porciones_posibles` FROM (`receta` `r` left join `receta_ingrediente` `ri` on(`ri`.`id_receta` = `r`.`id_receta`)) WHERE `r`.`activa` = 1 GROUP BY `r`.`id_receta`, `r`.`nombre`, `r`.`porciones_base` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_historial_transacciones`
--
DROP TABLE IF EXISTS `v_historial_transacciones`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_historial_transacciones`  AS SELECT `t`.`id_transaccion` AS `id_transaccion`, `t`.`fecha_hora` AS `fecha_hora`, `t`.`tipo` AS `tipo`, coalesce(`r`.`nombre`,group_concat(distinct `i`.`nombre` order by `i`.`nombre` ASC separator ', ')) AS `receta_o_ingredientes`, sum(`dt`.`cantidad`) AS `cantidad_total_movida`, `t`.`porciones_preparadas` AS `porciones_preparadas`, `t`.`descripcion` AS `descripcion` FROM ((((`transaccion` `t` left join `receta` `r` on(`r`.`id_receta` = `t`.`id_receta`)) left join `detalle_transaccion` `dt` on(`dt`.`id_transaccion` = `t`.`id_transaccion`)) left join `inventario_lote` `l` on(`l`.`id_lote` = `dt`.`id_lote`)) left join `ingrediente` `i` on(`i`.`id_ingrediente` = `l`.`id_ingrediente`)) GROUP BY `t`.`id_transaccion`, `t`.`fecha_hora`, `t`.`tipo`, `r`.`nombre`, `t`.`porciones_preparadas`, `t`.`descripcion` ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_lotes_por_caducar`
--
DROP TABLE IF EXISTS `v_lotes_por_caducar`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_lotes_por_caducar`  AS SELECT `l`.`id_lote` AS `id_lote`, `i`.`id_ingrediente` AS `id_ingrediente`, `i`.`nombre` AS `ingrediente`, `l`.`cantidad_actual` AS `cantidad_actual`, `u`.`abreviatura` AS `unidad`, `l`.`fecha_caducidad` AS `fecha_caducidad`, to_days(`l`.`fecha_caducidad`) - to_days(curdate()) AS `dias_restantes` FROM ((`inventario_lote` `l` join `ingrediente` `i` on(`i`.`id_ingrediente` = `l`.`id_ingrediente`)) join `unidad_medida` `u` on(`u`.`id_unidad` = `i`.`id_unidad`)) WHERE `l`.`cantidad_actual` > 0 AND `l`.`fecha_caducidad` between curdate() and curdate() + interval 7 day ;

-- --------------------------------------------------------

--
-- Estructura para la vista `v_stock_vigente`
--
DROP TABLE IF EXISTS `v_stock_vigente`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `v_stock_vigente`  AS SELECT `i`.`id_ingrediente` AS `id_ingrediente`, `i`.`nombre` AS `ingrediente`, `u`.`nombre` AS `unidad`, `u`.`abreviatura` AS `abreviatura`, sum(`l`.`cantidad_actual`) AS `existencia_vigente` FROM ((`ingrediente` `i` join `unidad_medida` `u` on(`u`.`id_unidad` = `i`.`id_unidad`)) join `inventario_lote` `l` on(`l`.`id_ingrediente` = `i`.`id_ingrediente`)) WHERE `i`.`activo` = 1 AND `l`.`cantidad_actual` > 0 AND (`l`.`fecha_caducidad` is null OR `l`.`fecha_caducidad` >= curdate()) GROUP BY `i`.`id_ingrediente`, `i`.`nombre`, `u`.`nombre`, `u`.`abreviatura` ;

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `detalle_transaccion`
--
ALTER TABLE `detalle_transaccion`
  ADD PRIMARY KEY (`id_detalle`),
  ADD KEY `idx_dt_transaccion` (`id_transaccion`),
  ADD KEY `idx_dt_lote` (`id_lote`);

--
-- Indices de la tabla `ingrediente`
--
ALTER TABLE `ingrediente`
  ADD PRIMARY KEY (`id_ingrediente`),
  ADD UNIQUE KEY `uq_ingrediente_nombre` (`nombre`),
  ADD KEY `fk_ingrediente_unidad` (`id_unidad`),
  ADD KEY `idx_ingrediente_activo` (`activo`);

--
-- Indices de la tabla `inventario_lote`
--
ALTER TABLE `inventario_lote`
  ADD PRIMARY KEY (`id_lote`),
  ADD KEY `idx_lote_ingrediente_caducidad` (`id_ingrediente`,`fecha_caducidad`),
  ADD KEY `idx_lote_caducidad` (`fecha_caducidad`);

--
-- Indices de la tabla `receta`
--
ALTER TABLE `receta`
  ADD PRIMARY KEY (`id_receta`),
  ADD UNIQUE KEY `uq_receta_nombre` (`nombre`),
  ADD KEY `idx_receta_activa` (`activa`);

--
-- Indices de la tabla `receta_ingrediente`
--
ALTER TABLE `receta_ingrediente`
  ADD PRIMARY KEY (`id_receta`,`id_ingrediente`),
  ADD KEY `idx_ri_ingrediente` (`id_ingrediente`);

--
-- Indices de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  ADD PRIMARY KEY (`id_transaccion`),
  ADD KEY `fk_trans_usuario` (`id_usuario`),
  ADD KEY `fk_trans_receta` (`id_receta`),
  ADD KEY `idx_trans_fecha_tipo` (`fecha_hora`,`tipo`);

--
-- Indices de la tabla `unidad_medida`
--
ALTER TABLE `unidad_medida`
  ADD PRIMARY KEY (`id_unidad`),
  ADD UNIQUE KEY `nombre` (`nombre`),
  ADD UNIQUE KEY `abreviatura` (`abreviatura`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`id_usuario`),
  ADD UNIQUE KEY `nombre_usuario` (`nombre_usuario`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `detalle_transaccion`
--
ALTER TABLE `detalle_transaccion`
  MODIFY `id_detalle` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `ingrediente`
--
ALTER TABLE `ingrediente`
  MODIFY `id_ingrediente` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `inventario_lote`
--
ALTER TABLE `inventario_lote`
  MODIFY `id_lote` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `receta`
--
ALTER TABLE `receta`
  MODIFY `id_receta` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `transaccion`
--
ALTER TABLE `transaccion`
  MODIFY `id_transaccion` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de la tabla `unidad_medida`
--
ALTER TABLE `unidad_medida`
  MODIFY `id_unidad` smallint(5) UNSIGNED NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `id_usuario` int(10) UNSIGNED NOT NULL AUTO_INCREMENT;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `detalle_transaccion`
--
ALTER TABLE `detalle_transaccion`
  ADD CONSTRAINT `fk_dt_lote` FOREIGN KEY (`id_lote`) REFERENCES `inventario_lote` (`id_lote`),
  ADD CONSTRAINT `fk_dt_transaccion` FOREIGN KEY (`id_transaccion`) REFERENCES `transaccion` (`id_transaccion`);

--
-- Filtros para la tabla `ingrediente`
--
ALTER TABLE `ingrediente`
  ADD CONSTRAINT `fk_ingrediente_unidad` FOREIGN KEY (`id_unidad`) REFERENCES `unidad_medida` (`id_unidad`);

--
-- Filtros para la tabla `inventario_lote`
--
ALTER TABLE `inventario_lote`
  ADD CONSTRAINT `fk_lote_ingrediente` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id_ingrediente`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `receta_ingrediente`
--
ALTER TABLE `receta_ingrediente`
  ADD CONSTRAINT `fk_ri_ingrediente` FOREIGN KEY (`id_ingrediente`) REFERENCES `ingrediente` (`id_ingrediente`) ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_ri_receta` FOREIGN KEY (`id_receta`) REFERENCES `receta` (`id_receta`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Filtros para la tabla `transaccion`
--
ALTER TABLE `transaccion`
  ADD CONSTRAINT `fk_trans_receta` FOREIGN KEY (`id_receta`) REFERENCES `receta` (`id_receta`),
  ADD CONSTRAINT `fk_trans_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`) ON DELETE SET NULL;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
