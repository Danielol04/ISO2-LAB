const hoy = new Date();
hoy.setHours(0, 0, 0, 0);
let fechaInicio = null, fechaFin = null;
let mesActual = new Date().getMonth();
let anoActual = new Date().getFullYear();
const nombresMeses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];
function renderCalendarios() {
    const contenedor = document.getElementById('contenedorMeses');
    contenedor.innerHTML = '';
    const mesSig = (mesActual + 1) % 12;
    const anoSig = mesActual === 11 ? anoActual + 1 : anoActual;
    contenedor.appendChild(crearCalendario(anoActual, mesActual));
    contenedor.appendChild(crearCalendario(anoSig, mesSig));
    actualizarVisuales();
}

function crearCalendario(ano, mes) {
    const div = document.createElement('div');
    div.classList.add('mes');
    const titulo = document.createElement('h3');
    titulo.textContent = `${nombresMeses[mes]} ${ano}`;
    div.appendChild(titulo);
    const tabla = document.createElement('table');
    const diasSemana = ["L", "M", "X", "J", "V", "S", "D"];
    let html = `<thead><tr>${diasSemana.map(d => `<th>${d}</th>`).join('')}</tr></thead><tbody><tr>`;
    const primerDia = new Date(ano, mes, 1);
    const offset = (primerDia.getDay() + 6) % 7;
    for (let i = 0; i < offset; i++) html += '<td></td>';
    let fecha = new Date(ano, mes, 1);
    while (fecha.getMonth() === mes) {
        const dia = fecha.getDate();
        const fechaCelda = new Date(ano, mes, dia);
        fechaCelda.setHours(0, 0, 0, 0);
        const str = `${ano}-${String(mes + 1).padStart(2, '0')}-${String(dia).padStart(2, '0')}`;
        const disponible = !fechasDisponibles.includes(str);
        const reservado = fechasReservadas.includes(str);
        const esPasado = fechaCelda < hoy;
        if (reservado) {
            html += `<td class='dia-reservado'>${dia}</td>`;
        } else if (!esPasado && disponible) {
            html += `<td onclick="seleccionarDia(${ano},${mes},${dia})">${dia}</td>`;
        } else {
            html += `<td class='dia-no-disponible'>${dia}</td>`;
        }
        if (fecha.getDay() === 0) html += '</tr><tr>';
        fecha.setDate(dia + 1);
    }
    html += '</tr></tbody>';
    tabla.innerHTML = html;
    div.appendChild(tabla);
    return div;
}


function seleccionarDia(ano, mes, dia) {
    const seleccion = new Date(ano, mes, dia);
    seleccion.setHours(0, 0, 0, 0);

    if (!fechaInicio || fechaFin) {
        fechaInicio = seleccion;
        fechaFin = null;
    } else {
        if (seleccion < fechaInicio) {
            fechaFin = fechaInicio;
            fechaInicio = seleccion;
        } else {
            fechaFin = seleccion;
        }
    }

    actualizarVisuales();

    document.getElementById('inputLlegada').value = fechaInicio ? formatear(fechaInicio) : '';
    document.getElementById('inputSalida').value = fechaFin ? formatear(fechaFin) : '';
}

function formatear(f) {
    return `${f.getFullYear()}-${String(f.getMonth() + 1).padStart(2, '0')}-${String(f.getDate()).padStart(2, '0')}`;
}

function actualizarVisuales() {
    const celdas = document.querySelectorAll('.calendario td');
    celdas.forEach(td => td.classList.remove('dia-seleccionado', 'rango'));

    if (!fechaInicio) return;

    const tds = document.querySelectorAll('.calendario td:not(.dia-no-disponible)');
    tds.forEach(td => {
        const dia = parseInt(td.textContent);
        if (isNaN(dia)) return;

        const tituloMes = td.closest('.mes').querySelector('h3').textContent.split(' ');
        const mesNombre = tituloMes[0];
        const ano = parseInt(tituloMes[1]);
        const mesIndex = nombresMeses.indexOf(mesNombre);
        const fechaCelda = new Date(ano, mesIndex, dia);
        fechaCelda.setHours(0, 0, 0, 0);

        if (fechaFin) {
            if (fechaCelda >= fechaInicio && fechaCelda <= fechaFin) {
                if (fechaCelda.getTime() === fechaInicio.getTime() || fechaCelda.getTime() === fechaFin.getTime()) {
                    td.classList.add('dia-seleccionado');
                } else {
                    td.classList.add('rango');
                }
            }
        } else if (fechaCelda.getTime() === fechaInicio.getTime()) {
            td.classList.add('dia-seleccionado');
        }
    });
}

document.getElementById('btnPrev').onclick = () => {
    mesActual--;
    if (mesActual < 0) { mesActual = 11; anoActual--; }
    renderCalendarios();
};

document.getElementById('btnNext').onclick = () => {
    mesActual++;
    if (mesActual > 11) { mesActual = 0; anoActual++; }
    renderCalendarios();
};

renderCalendarios();

let idAEliminar = null;
function confirmarEliminacion(id) {
    idAEliminar = id;
    document.getElementById("formEliminar").action = "/disponibilidades/eliminar/" + id;
    document.getElementById("popupEliminar").style.display = "flex";
}
function cerrarPopup() {
    document.getElementById("popupEliminar").style.display = "none";
    idAEliminar = null;
}

function mostrarPopupError(mensaje) {
    const popup = document.getElementById("popupError");
    document.getElementById("mensajeError").textContent = mensaje;

    popup.style.display = "flex";

    setTimeout(() => {
      popup.style.display = "none";
    }, 2000);
}

document.getElementById("formCrear").addEventListener("submit", function (event) {
    const inicio = document.getElementById("inputLlegada").value;
    const fin = document.getElementById("inputSalida").value;

    if (!inicio || !fin) {
      event.preventDefault();
      mostrarPopupError("Debes seleccionar la fecha de inicio y la fecha de fin.");
    }
});