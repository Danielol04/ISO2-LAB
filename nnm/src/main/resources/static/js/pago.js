function gestionarValidaciones(metodo) {
    const cardSection = document.getElementById('card-details-wrapper');
    const paypalSection = document.getElementById('paypal-details-wrapper');

    // Seleccionamos los inputs dentro de cada sección
    const cardInputs = cardSection.querySelectorAll('input');
    const paypalInput = paypalSection.querySelector('input[name="emailPaypal"]');

    if (metodo === 'TARJETA' || metodo === 'TARJETA_CREDITO') {
        // 1. Mostrar Tarjeta / Ocultar PayPal
        cardSection.style.display = 'block';
        paypalSection.style.display = 'none';

        // 2. Hacer OBLIGATORIOS los campos de tarjeta
        cardInputs.forEach(input => {
            input.setAttribute('required', '');
        });

        // 3. Quitar obligatorio al email de PayPal
        paypalInput.removeAttribute('required');

    } else if (metodo === 'PAYPAL') {
        // 1. Ocultar Tarjeta / Mostrar PayPal
        cardSection.style.display = 'none';
        paypalSection.style.display = 'block';

        // 2. Quitar obligatorio a los campos de tarjeta
        cardInputs.forEach(input => {
            input.removeAttribute('required');
        });

        // 3. Hacer OBLIGATORIO el email de PayPal
        paypalInput.setAttribute('required', '');
    }
}

document.addEventListener('DOMContentLoaded', function() {
    // Al cargar la página, gestionar las validaciones según el método seleccionado (permitir recargar pagina y no perder selección)
    const metodoSeleccionado = document.querySelector('input[name="metodoPago"]:checked').value;
    gestionarValidaciones(metodoSeleccionado);
});