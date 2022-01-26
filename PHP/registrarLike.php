<?php
$like = $_REQUEST['like'];
$usuario = $_REQUEST['idusuario'];
$libro = $_REQUEST['idlibro'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$insertarlike = $conexion->registrarLike($usuario,$libro,$like);
$resultado[]=array('registrado' => $insertarlike);
$mostrar =json_encode($resultado);
echo $mostrar;
?>