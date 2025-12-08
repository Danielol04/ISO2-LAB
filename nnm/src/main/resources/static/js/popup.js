
// Mostrar toast sencillo (exito | error)
window.mostrarMensaje = function (texto, tipo = "info") {
    // Reusar un contenedor si ya existe
    let cont = document.getElementById('toast-contenedor');
    if (!cont) {
        cont = document.createElement('div');
        cont.id = 'toast-contenedor';
        document.body.appendChild(cont);
        cont.style.position = 'fixed';
        cont.style.top = '20px';
        cont.style.right = '20px';
        cont.style.zIndex = '9999';
    }

    const msg = document.createElement('div');
    msg.className = 'toast-mensaje ' + tipo;
    msg.textContent = texto;
    msg.style.marginTop = '8px';
    msg.style.padding = '10px 14px';
    msg.style.borderRadius = '8px';
    msg.style.boxShadow = '0 4px 10px rgba(0,0,0,0.15)';
    msg.style.color = '#fff';
    msg.style.fontWeight = '600';
    msg.style.opacity = '0';
    msg.style.transition = 'transform 0.2s ease, opacity 0.2s ease';
    if (tipo === 'error') {
        msg.style.background = '#e74c3c';
    } else if (tipo === 'exito') {
        msg.style.background = '#27ae60';
    } else {
        msg.style.background = '#333';
    }

    cont.appendChild(msg);

    // animación entrada
    requestAnimationFrame(() => {
        msg.style.opacity = '1';
        msg.style.transform = 'translateY(0)';
    });

    // desaparecer
    setTimeout(() => {
        msg.style.opacity = '0';
        msg.style.transform = 'translateY(-10px)';
        setTimeout(() => msg.remove(), 300);
    }, 2000);
};


let callbackConfirmar = null;
function mostrarConfirmacionGeneral(mensaje, onConfirmar) {
    callbackConfirmar = onConfirmar;

    document.getElementById("mensajeConfirmacion").textContent = mensaje;
    document.getElementById("popupConfirmacion").style.display = "flex";
}

document.getElementById("btnConfirmar").addEventListener("click", () => {
    document.getElementById("popupConfirmacion").style.display = "none";
    if (callbackConfirmar) callbackConfirmar();
});

document.getElementById("btnCancelar").addEventListener("click", () => {
    document.getElementById("popupConfirmacion").style.display = "none";
    callbackConfirmar = null;
});
