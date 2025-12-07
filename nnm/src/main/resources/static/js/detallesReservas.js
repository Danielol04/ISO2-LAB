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

    items.forEach(item => {
        item.addEventListener("click", () => {

            items.forEach(i => i.classList.remove("seleccionada"));
            item.classList.add("seleccionada");

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
        });
    });
});