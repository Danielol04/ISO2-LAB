function mostrarFormulario(metodo) {
    const cardSection = document.getElementById('card-details-wrapper');
    const paypalSection = document.getElementById('paypal-details-wrapper');

    // Si es cualquier tipo de tarjeta, mostramos el formulario de tarjeta
    if (metodo === 'TARJETA') {
        cardSection.style.display = 'block';
        paypalSection.style.display = 'none';
    } 
    // Si es PayPal, mostramos el bloque azul estilo login
    else if (metodo === 'PAYPAL') {
        cardSection.style.display = 'none';
        paypalSection.style.display = 'block';
    }
}