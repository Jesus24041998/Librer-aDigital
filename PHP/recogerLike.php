<?php
$titulo = $_REQUEST['titulo'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$recogerlikes = $conexion->obtenerLikeLibro($titulo);
$mostrar =json_encode($recogerlikes);
echo $mostrar;
?>