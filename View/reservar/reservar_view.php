<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Drivo | Reservar Vehículo</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.8/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.11.3/font/bootstrap-icons.min.css">
    <link rel="stylesheet" href="../View/css/style.css">
    <link rel="stylesheet" href="../View/css/reservar.css">
    <link rel="shortcut icon" href="../View/img/logo.png" type="image/x-icon">
</head>
<body>
    <?php include '../View/header.php' ?>

    <main class="main__container reserva-container">
        
        <?php if (isset($_SESSION['mensaje_error'])): ?>
            <div class="alert alert-danger alert-dismissible fade show rounded-custom" role="alert">
                <i class="bi bi-exclamation-triangle-fill me-2"></i> <?= $_SESSION['mensaje_error'] ?>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <?php unset($_SESSION['mensaje_error']); ?>
        <?php endif; ?>

        <div class="row justify-content-center mb-5">
            <div class="col-lg-6">
                <div class="oferta p-4 w-100 position-relative shadow-sm">
                    <h2 class="modelo modelo-reserva text-center text-uppercase fw-bold mb-4">
                        ¿Cuándo quieres tu coche?
                    </h2>

                    <form action="../Controller/reservar.php" method="POST" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="buscar">
                        
                        <div id="alerta_reserva" class="alert alert-warning d-none rounded-custom mt-2" role="alert">
                            <i class="bi bi-exclamation-triangle-fill me-2"></i> <span id="mensaje_alerta"></span>
                        </div>

                        <div class="mb-3">
                            <label class="form-label fw-bold">Fecha de Recogida</label>
                            <input type="date" id="fecha_inicio" name="fecha_inicio" class="form-control rounded-custom" 
                                   value="<?= htmlspecialchars($fecha_inicio ?? '') ?>" required onchange="validarFechas()">
                        </div>

                        <div class="mb-4">
                            <label class="form-label fw-bold">Fecha de Devolución</label>
                            <input type="date" id="fecha_fin" name="fecha_fin" class="form-control rounded-custom" 
                                   value="<?= htmlspecialchars($fecha_fin ?? '') ?>" required onchange="validarFechas()">
                        </div>

                        <div class="reservar__container m-0 p-1">
                            <button type="submit" id="btn-buscar" class="btn-full text-uppercase fw-bold">
                                Buscar vehículos disponibles
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>

        <?php if (isset($busquedaRealizada) && $busquedaRealizada): ?>
            <hr class="my-5" style="border-color: #152D51; opacity: 0.2;">
            
            <div class="mb-4">
                <h3 class="fw-bold text-primary-drivo text-uppercase">Vehículos Disponibles</h3>
                <p class="text-muted">Resultados libres para el periodo: <strong class="text-secondary-drivo"><?= date("d/m/Y", strtotime($fecha_inicio)) ?></strong> al <strong class="text-secondary-drivo"><?= date("d/m/Y", strtotime($fecha_fin)) ?></strong></p>
            </div>

            <?php if (empty($vehiculosDisponibles)): ?>
                <div class="alert alert-warning rounded-custom p-4 border-0 shadow-sm" role="alert">
                    <i class="bi bi-exclamation-circle-fill me-2 fs-5"></i> Lo sentimos, no quedan vehículos libres para el rango de fechas seleccionado. Intente con otras fechas.
                </div>
            <?php else: ?>
                <div class="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
                    <?php foreach ($vehiculosDisponibles as $coche): ?>
                        <div class="col">
                            <div class="card h-100 shadow-sm border-0 rounded-custom p-3 bg-white" style="transition: transform 0.2s;">
                                
                                <img src="../View/img/coches/<?= pathinfo($coche->getImagen(), PATHINFO_FILENAME) ?>--sin_fondo.png" 
                                     class="card-img-top img-fluid p-2" 
                                     alt="<?= $coche->getNombreCompleto() ?>" 
                                     style="max-height: 240px; object-fit: contain;"> 
                                
                                <div class="card-body d-flex flex-column p-2">
                                    <h5 class="card-title fw-bold text-uppercase text-primary-drivo m-0 mb-2"><?= $coche->getNombreCompleto() ?></h5>
                                    
                                    <div class="row text-muted small g-2 mb-3">
                                        <div class="col-6"><i class="bi bi-speedometer2 me-1"></i><?= $coche->getMotor() ?></div>
                                        <div class="col-6"><i class="bi bi-gear-wide-connected me-1"></i><?= $coche->getCambios() ?></div>
                                    </div>

                                    <p class="text-success fw-bold fs-5 mb-3 mt-auto"><?= $coche->getPrecioDia() ?>€ <span class="fs-6 text-muted fw-normal">/ día</span></p>
                                    
                                    <form action="../Controller/pago.php" method="POST">
    <input type="hidden" name="id_coche" value="<?= $coche->getId() ?>">
    
    <input type="hidden" name="fecha_inicio" value="<?= htmlspecialchars($fecha_inicio) ?>">
    <input type="hidden" name="fecha_fin" value="<?= htmlspecialchars($fecha_fin) ?>">
    
    <button type="submit" class="btn-full py-2 fs-6">
        RESERVAR ESTE COCHE
    </button>
</form>
                                </div>
                            </div>
                        </div>
                    <?php endforeach; ?>
                </div>
            <?php endif; ?>
        <?php endif; ?>

    </main>

    <?php include '../View/footer.php' ?>

    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const fInicio = document.getElementById('fecha_inicio');
            const fFin = document.getElementById('fecha_fin');

            const hoy = new Date().toISOString().split('T')[0];
            console.log("Fecha Inicio " + hoy);
            fInicio.min = hoy;
            console.log("Fecha Inicio " + fInicio.min);

            const hoyObj = new Date();
            const maxInicioObj = new Date();
            maxInicioObj.setMonth(hoyObj.getMonth() + 10);
            console.log("Segundos" + maxInicioObj.setMonth(hoyObj.getMonth() + 10));
            fInicio.max = maxInicioObj.toISOString().split('T')[0];
            console.log("Fecha fin " + fInicio.max);
        });

        function validarFechas() {
            const fInicio = document.getElementById('fecha_inicio');
            const fFin = document.getElementById('fecha_fin');
            const btnSubmit = document.getElementById('btn-buscar');
            const alertaReserva = document.getElementById('alerta_reserva');
            const mensajeAlerta = document.getElementById('mensaje_alerta');

            fInicio.classList.remove('is-invalid');
            fFin.classList.remove('is-invalid');
            alertaReserva.classList.add('d-none');
            btnSubmit.disabled = false;

            if (fInicio.value) {
                fFin.min = fInicio.value;
                if (fFin.value && fFin.value < fInicio.value) {
                    fFin.value = fInicio.value;
                }
            }

            if (fInicio.value && fFin.value) {
                const dateInicio = new Date(fInicio.value);
                const dateFin = new Date(fFin.value);

                const maxFinPermitido = new Date(dateInicio);
                maxFinPermitido.setMonth(maxFinPermitido.getMonth() + 3);

                if (dateFin > maxFinPermitido) {
                    mensajeAlerta.innerText = "La duración máxima del alquiler no puede supercar los 3 meses.";
                    alertaReserva.classList.remove('d-none');
                    
                    fInicio.classList.add('is-invalid');
                    fFin.classList.add('is-invalid');
                    btnSubmit.disabled = true;
                }
            }
        }

        (() => {
          'use strict'
          const forms = document.querySelectorAll('.needs-validation')
          Array.from(forms).forEach(form => {
            form.addEventListener('submit', event => {
              if (!form.checkValidity()) {
                event.preventDefault()
                event.stopPropagation()
              }
              form.classList.add('was-validated')
            }, false)
          })
        })()
    </script>
</body>
</html>