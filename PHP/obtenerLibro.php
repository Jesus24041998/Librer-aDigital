<?php
require_once 'Conexion.php';
$conexion = new ConexionBD();
$titulo = $_REQUEST['titulo'];
$libro = $conexion->obtenerLikeLibro($titulo);
$mostrar =json_encode($libro);
echo $mostrar;
?>