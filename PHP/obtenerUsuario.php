<?php
$correo = $_REQUEST['correo'];
$contrasena = $_REQUEST['contraseña'];
require_once 'Conexion.php';
$conexion = new ConexionBD();
$usuarios = $conexion->obtenerUsuario($correo,$contrasena);
$mostrar =json_encode($usuarios);
echo $mostrar;
?>