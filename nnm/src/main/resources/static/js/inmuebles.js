
// Función para eliminar inmueble
eliminarInmueble = function(id) {

    mostrarConfirmacionGeneral("Esta acción eliminará el inmueble permanentemente. ¿Deseas continuar?", () => {

        fetch(`/inmuebles/eliminar/${id}`, {
            method: 'DELETE'
        })
        .then(r => r.json().catch(() => ({ exito: r.ok, id })))
        .then(data => {
            if (data.exito) {
                const div = document.getElementById(`inmueble-${id}`);
                if (div) div.remove();
                mostrarMensaje("Inmueble eliminado correctamente", "exito");
            } else {
                mostrarMensaje("Error al eliminar el inmueble", "error");
            }
        })
        .catch(() => mostrarMensaje("Error de conexión"), "error");
    });
};


