let idReservaACancelar = null;

function confirmarCancelarReserva(id) {
    idReservaACancelar = id;
    const form = document.getElementById("formCancelarReserva");
    form.action = `/reserva/cancelar/${id}`;
    document.getElementById("popupCancelarReserva").style.display = "flex";
}

function cerrarPopupCancelar() {
    document.getElementById("popupCancelarReserva").style.display = "none";
    idReservaACancelar = null;
}

document.addEventListener("DOMContentLoaded", () => {
    const items = document.querySelectorAll(".solicitud-item");
    const detalle = document.querySelector(".detalle-solicitud");

    if (!detalle) return;

    const placeholder = detalle.querySelector(".mensaje-placeholder");
    const detalleInfo = detalle.querySelector("#detalleInfo");

    const tituloElem = detalle.querySelector("#mdTitulo");
    const imgElem = detalle.querySelector("#mdImg");
    const fechaInicioElem = detalle.querySelector("#mdFechaInicio");
    const fechaFinElem = detalle.querySelector("#mdFechaFin");
    const nochesElem = detalle.querySelector("#mdNoches");
    const propietarioElem = detalle.querySelector("#mdPropietario");
    const precioNocheElem = detalle.querySelector("#mdPrecioNoche");
    const totalElem = detalle.querySelector("#mdPrecioTotal");
    const btnCancelar = detalle.querySelector("#btnCancelarReserva");

    items.forEach(item => {
        item.addEventListener("click", () => {
            items.forEach(i => i.classList.remove("seleccionada"));
            item.classList.add("seleccionada");

            // Actualizar detalle
            tituloElem.textContent = item.dataset.titulo;
            imgElem.src = item.dataset.img;
            fechaInicioElem.textContent = item.dataset.fechainicio;
            fechaFinElem.textContent = item.dataset.fechafin;
            nochesElem.textContent = item.dataset.noches;
            propietarioElem.textContent = item.dataset.propietario;
            precioNocheElem.textContent = item.dataset.precionoche;
            totalElem.textContent = item.dataset.preciototal;

            placeholder.style.display = "none";
            detalleInfo.classList.remove("hidden");

            // Comprobar si se puede cancelar
            const hoy = new Date();
            hoy.setHours(0, 0, 0, 0);
            const fechaInicio = new Date(item.dataset.fechainicio);
            fechaInicio.setHours(0, 0, 0, 0);
            const diferenciaDias = (fechaInicio - hoy) / (1000 * 60 * 60 * 24);

            if (diferenciaDias >= 2) {
                btnCancelar.style.display = "inline-block";
                btnCancelar.onclick = () => confirmarCancelarReserva(item.dataset.id);
            } else {
                btnCancelar.style.display = "none";
            }
        });
    });
});