
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
    }, 2800);
};

// Función global para eliminar inmueble
window.eliminarInmueble = function(id) {

    mostrarConfirmacion("¿Estás seguro de eliminar este inmueble?", () => {

        fetch(`/inmuebles/eliminar/${id}`, {
            method: 'DELETE'
        })
        .then(r => r.json().catch(() => ({ exito: r.ok, id })))
        .then(data => {
            if (data.exito) {
                const div = document.getElementById(`inmueble-${id}`);
                if (div) div.remove();
                mostrarMensaje("Inmueble eliminado", "exito");
            } else {
                mostrarMensaje("Error eliminando inmueble", "error");
            }
        })
        .catch(() => mostrarMensaje("Error de conexión", "error"));
    });
};

window.mostrarConfirmacion = function (texto, callbackAceptar) {
    const modal = document.getElementById("ventana-confirmacion");
    const textoElem = document.getElementById("vc-texto");
    const btnAceptar = document.getElementById("vc-aceptar");
    const btnCancelar = document.getElementById("vc-cancelar");

    textoElem.textContent = texto;
    modal.style.display = "flex";

    const aceptar = () => {
        modal.style.display = "none";
        btnAceptar.removeEventListener("click", aceptar);
        btnCancelar.removeEventListener("click", cancelar);
        callbackAceptar();
    };

    const cancelar = () => {
        modal.style.display = "none";
        btnAceptar.removeEventListener("click", aceptar);
        btnCancelar.removeEventListener("click", cancelar);
    };

    btnAceptar.addEventListener("click", aceptar);
    btnCancelar.addEventListener("click", cancelar);
};

