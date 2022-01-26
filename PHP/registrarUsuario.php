<?php
$correo = $_REQUEST['correo'];
$contrasena = $_REQUEST['contraseña'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$usuarios = $conexion->registrarUsuario($correo,$contrasena);
$resultado[]=array('registrado' => $usuarios);

$mostrar =json_encode($resultado);
echo $mostrar;
?>