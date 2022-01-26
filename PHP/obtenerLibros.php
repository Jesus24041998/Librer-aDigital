<?php
require_once 'Conexion.php';
$conexion = new ConexionBD();
$libros = $conexion->obtenerLibros();
$mostrar =json_encode($libros);
echo $mostrar;
?>