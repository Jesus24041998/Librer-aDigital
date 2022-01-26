<?php
define("DB_HOST", "localhost");
define("DB_DATABASE", "dblibreriaelectronica");
define("DB_USER", "root");
define("DB_PASSWORD", "");
class ConexionBD
{
/**
* Description: Constructor de la clase
* Conecta automáticamente con la base de datos
*/
function __construct()
{
}
/**
* Description: Destructor de la clase
* Cierra la conexión automáticamente con la base de datos
*/
function __destruct()
{
}

public function recogerLikeUsuario($usuario,$libro)
{
// Conecto con la base de datos
$enlace = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Verificar la conexión
if (mysqli_connect_errno())
{
printf("Falló la conexión: %s\n", mysqli_connect_error());
exit();
}

// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");

$sql = sprintf("SELECT count(*) FROM likes where idusuario = '$usuario' and idlibro = '$libro'");
$resultado = mysqli_query($enlace, $sql);
$fila = $resultado->fetch_row();
$numero = $fila[0];
$datos = array();

if($numero == 1)
{
$datos[] = array('resultado' => true);
}
else{
$datos[] = array('resultado' => false);

}
// Liberar el conjunto de resultados
$resultado->close();
// Cierro la conexión con la base de datos
mysqli_close($enlace);
return $datos;
}

public function registrarUsuario($correo, $contrasena)
{
// Conecto con la base de datos
$conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Compruebo la conexión
if ($conn->connect_error)
{
die("Connection failed: " . $conn->connect_error);
}
$identificador=uniqid('user_');
$contrasenacifrada = password_hash($contrasena, PASSWORD_DEFAULT);

$sql = "INSERT INTO usuarios (identificador,correo, contrasena) VALUES ('$identificador','$correo','$contrasenacifrada')";
// Ejecuto la consulta de insertar
if ($conn->query($sql) === TRUE)
{
$devolver = "ok";
}
else
{
$devolver = "no ok";
}
// Cierro la conexión con la base de datos
mysqli_close($conn);
return $devolver;
}

public function registrarLibro($titulo, $caratula,$autor)
{
// Conecto con la base de datos
$conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Compruebo la conexión
if ($conn->connect_error)
{
die("Connection failed: " . $conn->connect_error);
}

$sql = "INSERT INTO libros (caratula, titulo,autor,likes) VALUES ('$caratula','$titulo', '$autor',0)";
// Ejecuto la consulta de insertar
if ($conn->query($sql) === TRUE)
{
$devolver = "ok";
}
else
{
$devolver = "no ok";
}
// Cierro la conexión
mysqli_close($conn);
return $devolver;
}


public function obtenerLibros()
{
// Conecto con la base de datos
$enlace = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Verificar la conexión
if (mysqli_connect_errno())
{
printf("Falló la conexión: %s\n", mysqli_connect_error());
exit();
}

// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");

$sql = sprintf("SELECT * FROM libros");
$resultado = mysqli_query($enlace, $sql);
$fila = $resultado->fetch_row();

$datos = array();

if ($resultado = mysqli_query($enlace, $sql))
{
// Obtener el array de objetos
while ($fila = $resultado->fetch_row())
{
$datos[] = array('id' => $fila[0], 'caratula' => $fila[1],'titulo' => $fila[2],'autor' => $fila[3],'likes' => $fila[4]);
}
// Liberar el conjunto de resultados
$resultado->close();
}
else
{
	$datos[] = array('incorrecta'=>'incorrecta');
}
// Cierro la conexión con la base de datos
mysqli_close($enlace);
return $datos;
}

public function obtenerLikeLibro($titulo)
{
// Conecto con la base de datos
$enlace = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Verificar la conexión
if (mysqli_connect_errno())
{
printf("Falló la conexión: %s\n", mysqli_connect_error());
exit();
}

// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");

$sql = sprintf("SELECT likes FROM libros where titulo = '$titulo'");
$resultado = mysqli_query($enlace, $sql);
$fila = $resultado->fetch_row();

$datos = array();

if ($resultado = mysqli_query($enlace, $sql))
{
// Obtener el array de objetos
while ($fila = $resultado->fetch_row())
{
$datos[] = array('likes' => $fila[0]);
}
// Liberar el conjunto de resultados
$resultado->close();
}
else
{
	$datos[] = array('incorrecta'=>'incorrecta');
}
// Cierro la conexión con la base de datos
mysqli_close($enlace);
return $datos;
}

public function obtenerUsuario($correo,$contrasena)
{
// Conecto con la base de datos
$enlace = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Verificar la conexión
if (mysqli_connect_errno())
{
printf("Falló la conexión: %s\n", mysqli_connect_error());
exit();
}

// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");

$sql = sprintf("SELECT count(*) FROM usuarios");
$resultado = mysqli_query($enlace, $sql);
$fila = $resultado->fetch_row();
if($fila[0]>0)
{
	$sql2 = sprintf("SELECT * FROM usuarios where correo = '$correo'");	
	$resultado2 = mysqli_query($enlace, $sql2);
	$fila2 = $resultado2->fetch_row();
if(password_verify(trim($contrasena),$fila2[2]))
{
$datos = array();
// Obtener el array de objetos

$resultado2 = mysqli_query($enlace, $sql2);	
while ($fila2 = $resultado2->fetch_row())
{
$datos[] = array('identificador' => $fila2[0], 'correo' => $fila2[1]);
}
// Liberar el conjunto de resultados
}
else{
	$datos[] = array('identificador'=>'');
}
}
else{
	$datos[] = array('identificador'=>'');
}
$resultado->close();
$resultado2->close();
// Cierro la conexión con la base de datos
mysqli_close($enlace);
return $datos;
}
public function registrarLike($usuario,$libro,$like)
{
// Conecto con la base de datos
$conn = new mysqli(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Compruebo la conexión
if ($conn->connect_error)
{
die("Connection failed: " . $conn->connect_error);
}
$conn->set_charset("utf8");
$sql = sprintf("SELECT count(*) FROM likes where idusuario = '$usuario' and idlibro = '$libro'");
$resultado = mysqli_query($conn, $sql);
$fila = $resultado->fetch_row();
if($like == 'true')
{
if($fila[0]==0)
{
	$resultado->close();
	$sql = "INSERT INTO likes (numerolikes,idusuario,idlibro) VALUES ('1','$usuario', '$libro')";
	if ($conn->query($sql) === TRUE)
	{
		$devolver = "ok";
		$conexion = new ConexionBD();
		$actualizarLibro = $conexion->actualizarLibro($libro,$conn);
	}
	else
	{
		$devolver = "no ok";
	}
}
}
else{
	$sql ="DELETE FROM likes WHERE idusuario = '$usuario' and idlibro = '$libro'";
	if ($conn->query($sql) === TRUE)
	{
		$devolver = "ok";
		$conexion = new ConexionBD();
		$actualizarLibro = $conexion->actualizarLibro($libro,$conn);
	}
	else
	{
		$devolver = "no ok";
	}
}
return $devolver;
}

public function actualizarLibro($libro,$enlace)
{
// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");
$sql = sprintf("SELECT SUM(numerolikes) FROM likes where idlibro = '$libro'");

$resultado = mysqli_query($enlace, $sql);

$fila = $resultado->fetch_row();

$sql2 = "UPDATE libros set likes = '$fila[0]' where id = '$libro'";
$enlace->query($sql2);
$resultado->close();
// Cierro la conexión
mysqli_close($enlace);
}

public function recogerLibrosSegunLike($usuario)
{
//Arreglar
// Conecto con la base de datos
$enlace = mysqli_connect(DB_HOST, DB_USER, DB_PASSWORD, DB_DATABASE);
// Verificar la conexión
if (mysqli_connect_errno())
{
printf("Falló la conexión: %s\n", mysqli_connect_error());
exit();
}

// Cambio el conjunto de caracteres a utf8
$enlace->set_charset("utf8");

$sql = sprintf("SELECT count(*) FROM likes where idusuario = '$usuario'");
$resultado = mysqli_query($enlace, $sql);
$fila = $resultado->fetch_row();
$datos = array();
if($fila[0]!=0)
{
	$sql2 = sprintf("SELECT idlibro FROM likes where idusuario = '$usuario'");
	$resultado2 = mysqli_query($enlace, $sql2);	
	while ($fila2 = $resultado2->fetch_row())
	{
		$sql3 = sprintf("SELECT titulo,autor FROM libros where id = '$fila2[0]'");
		$resultado3 = mysqli_query($enlace, $sql3);
		while ($fila3 = $resultado3->fetch_row())
		{
			$datos[] = array('titulo' => $fila3[0], 'autor' => $fila3[1]);
		}
	}
	$resultado->close();
	$resultado2->close();
	$resultado3->close();
	
}

// Cierro la conexión con la base de datos
mysqli_close($enlace);
return $datos;
}
}
?>