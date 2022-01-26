<?php
$usuario = $_REQUEST['idusuario'];
$libro = $_REQUEST['idlibro'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$insertarlike = $conexion->recogerLikeUsuario($usuario,$libro);
$mostrar =json_encode($insertarlike);
echo $mostrar;
?>