<?php
require_once 'auth.php';
require_once '../Model/Reserva.php';
require_once '../Model/Vehiculo.php';

$busquedaRealizada = false;
$vehiculosDisponibles = [];
$fecha_inicio = '';
$fecha_fin = '';

// Si el usuario rellena el formulario de la opción de menú "Reservar" y pulsa "Buscar"
if (isset($_POST['action']) && $_POST['action'] === 'buscar') {
    $fecha_inicio = $_POST['fecha_inicio'];
    $fecha_fin = $_POST['fecha_fin'];

    if (!empty($fecha_inicio) && !empty($fecha_fin) && $fecha_fin >= $fecha_inicio) {
        // Llamamos al método inteligente que filtra en la base de datos
        $vehiculosDisponibles = Reserva::getVehiculosDisponibles($fecha_inicio, $fecha_fin);
        $busquedaRealizada = true;
    } else {
        $error_fechas = "Las fechas seleccionadas no son válidas.";
    }
}

// Cargamos la nueva vista unificada que muestra el formulario y los resultados abajo
include '../View/reservar/reservar_view.php';
?>