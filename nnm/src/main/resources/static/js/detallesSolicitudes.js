document.addEventListener("DOMContentLoaded", () => {
    const items = document.querySelectorAll(".solicitud-item");
    const detalle = document.querySelector(".detalle-solicitud");

    if (!detalle) return;

    // Elementos dentro del detalle
    const placeholder = detalle.querySelector(".mensaje-placeholder");
    const detalleInfo = detalle.querySelector("#detalleInfo");
    const tituloElem = detalle.querySelector("#mdTitulo");
    const imgElem = detalle.querySelector("#mdImg");
    const fechaInicioElem = detalle.querySelector("#mdFechaInicio");
    const fechaFinElem = detalle.querySelector("#mdFechaFin");
    const nochesElem = detalle.querySelector("#mdNoches");
    const inquilinoElem = detalle.querySelector("#mdNombre");
    const precioNocheElem = detalle.querySelector("#mdPrecioNoche");
    const totalElem = detalle.querySelector("#mdPrecioTotal");

    items.forEach(item => {
        item.addEventListener("click", () => {
            // Quitar clase seleccionada de otros items
            items.forEach(i => i.classList.remove("seleccionada"));
            item.classList.add("seleccionada");

            // Actualizar contenido del detalle
            if (tituloElem) tituloElem.textContent = item.dataset.titulo;

            if (imgElem) {
                imgElem.src = item.dataset.img;

                // Aseguramos que la imagen esté dentro de un contenedor con ratio
                if (!imgElem.parentElement.classList.contains("img-container")) {
                    const wrapper = document.createElement("div");
                    wrapper.classList.add("img-container");
                    imgElem.parentNode.insertBefore(wrapper, imgElem);
                    wrapper.appendChild(imgElem);
                }
            }

            if (fechaInicioElem) fechaInicioElem.textContent = item.dataset.fechainicio;
            if (fechaFinElem) fechaFinElem.textContent = item.dataset.fechafin;
            if (nochesElem) nochesElem.textContent = item.dataset.noches;
            if (inquilinoElem) inquilinoElem.textContent = item.dataset.nombre;
            if (precioNocheElem) precioNocheElem.textContent = item.dataset.precionoche;
            if (totalElem) totalElem.textContent = item.dataset.preciototal;

            // Mostrar detalle y ocultar placeholder
            if (placeholder) placeholder.style.display = "none";
            if (detalleInfo) detalleInfo.classList.remove("hidden");
        });
    });
});