<?php
$titulo = $_REQUEST['titulo'];
$caratula = $_REQUEST['caratula'];
$autor = $_REQUEST['autor'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$usuarios = $conexion->registrarLibro($titulo,$caratula,$autor);
$resultado[]=array('registrado' => $usuarios);
$mostrar =json_encode($resultado);
echo $mostrar;
?>