<?php
require_once 'Conexion.php';
$conexion = new ConexionBD();
$usuario = $_REQUEST['idusua'];
$libros = $conexion->recogerLibrosSegunLike($usuario);
$mostrar =json_encode($libros);
echo $mostrar;
?>