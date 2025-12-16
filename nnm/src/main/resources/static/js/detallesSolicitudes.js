document.addEventListener("DOMContentLoaded", () => {
    // CAMBIO IMPORTANTE: Buscamos .bandeja-item y .detalle-bandeja
    const items = document.querySelectorAll(".bandeja-item");
    const detalle = document.querySelector(".detalle-bandeja");

    if (!detalle) return;

    const placeholder = detalle.querySelector(".mensaje-placeholder");
    const detalleInfo = detalle.querySelector("#detalleInfo");
    
    // Selectores internos (estos siguen igual porque usan ID)
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
            items.forEach(i => i.classList.remove("seleccionada"));
            item.classList.add("seleccionada");

            if (tituloElem) tituloElem.textContent = item.dataset.titulo;
            
            if (imgElem) {
                imgElem.src = item.dataset.img;
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

            if (placeholder) placeholder.style.display = "none";
            if (detalleInfo) detalleInfo.classList.remove("hidden");

            const formAceptar = detalle.querySelector("#formAceptar");
            const formRechazar = detalle.querySelector("#formRechazar");

            if (formAceptar) {
                formAceptar.style.display = "block";
                formAceptar.action = `/solicitudes/solicitud/${item.dataset.id}/aceptar`;
            }

            if (formRechazar) {
                formRechazar.style.display = "block";
                formRechazar.action = `/solicitudes/solicitud/${item.dataset.id}/rechazar`;
            }
        });
    });
});