<?php
require_once 'Conexion.php';
$conexion = new ConexionBD();
$idusuario = $_REQUEST['idusua'];
$librolike = $conexion->obtenerLikeLibro($idusuario);
$mostrar =json_encode($librolike);
echo $mostrar;
?>