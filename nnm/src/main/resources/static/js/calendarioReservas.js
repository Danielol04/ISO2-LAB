console.log("Disponibles:", typeof fechasDisponibles !== 'undefined' ? fechasDisponibles : "NO LLEGA");
console.log("Reservadas:", typeof fechasReservadas !== 'undefined' ? fechasReservadas : "NO LLEGA");
const hoy = new Date();
    hoy.setHours(0, 0, 0, 0);

    let fechaInicio = null;
    let fechaFin = null;
    let mesActual = new Date().getMonth();
    let anoActual = new Date().getFullYear();

    const nombresMeses = ["Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
      "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"];

    const inputLlegada = document.getElementById('inputLlegada');
    const inputSalida = document.getElementById('inputSalida');
    const calendario = document.getElementById('calendarioRango');

    [inputLlegada, inputSalida].forEach(input => {
      input.addEventListener('click', () => {
        calendario.style.display = calendario.style.display === 'block' ? 'none' : 'block';
        renderCalendarios();
      });
    });

    document.addEventListener('click', e => {
      if (!e.target.closest('.calendario') &&
        !e.target.closest('#inputLlegada') &&
        !e.target.closest('#inputSalida')) {
        calendario.style.display = 'none';
      }
    });

    function renderCalendarios() {
      const contenedor = document.getElementById('contenedorMeses');
      contenedor.innerHTML = '';

      const mesSig = (mesActual + 1) % 12;
      const anoSig = mesActual === 11 ? anoActual + 1 : anoActual;

      contenedor.appendChild(crearCalendario(anoActual, mesActual));
      contenedor.appendChild(crearCalendario(anoSig, mesSig));

      document.getElementById('tituloMeses').textContent =
        `${nombresMeses[mesActual]} ${anoActual} – ${nombresMeses[mesSig]} ${anoSig}`;

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

        const disponible = fechasDisponibles.includes(str);
        const esPasado = fechaCelda < hoy;
        const reservado = fechasReservadas.includes(str);

        if (reservado) {
          html += `<td class="dia-no-disponible">${dia}</td>`;
        }
        else if (!esPasado && disponible) {
          html += `<td onclick="seleccionarDia(${ano},${mes},${dia}, this)">${dia}</td>`;        
        }else {
          html += `<td class="dia-no-disponible">${dia}</td>`;
        }

        if (fecha.getDay() === 0) html += '</tr><tr>';
        fecha.setDate(dia + 1);
      }

      html += '</tr></tbody>';
      tabla.innerHTML = html;
      div.appendChild(tabla);

      return div;
    }

    function seleccionarDia(ano, mes, dia, celdaPulsada) {
      const seleccion = new Date(ano, mes, dia);

      if (!fechaInicio || fechaFin) {
        fechaInicio = seleccion;
        fechaFin = null;
      } else {
        if(seleccion < fechaInicio) {
          fechaFin = fechaInicio;
          fechaInicio = seleccion;
        } else if(seleccion.getTime() === fechaInicio.getTime()) {
          fechaFin=null;
        }
        else {
          fechaFin = seleccion;
        }
        
        let iterador= new Date(fechaInicio);
        let hayConflicto= false;

        while(iterador<=fechaFin){
          if(fechasReservadas.includes(formatear(iterador))||!fechasDisponibles.includes(formatear(iterador))){
            hayConflicto= true;
            break;
          }
          iterador.setDate(iterador.getDate() + 1);
        }
      
        if(hayConflicto){
          animarError(celdaPulsada);
          fechaFin= null;
        }
      }
      actualizarVisuales();

      if (fechaInicio && fechaFin) {
        inputLlegada.value = formatear(fechaInicio);
        inputSalida.value = formatear(fechaFin);
        calendario.style.display = 'none';
      }
    }

    function animarError(celdaPulsada) {
    if (!celdaPulsada) return;
    
    celdaPulsada.classList.add('error-visual');

    setTimeout(() => {celdaPulsada.classList.remove('error-visual');}, 500);
    }

    function formatear(f) {
      return `${f.getFullYear()}-${String(f.getMonth() + 1).padStart(2, '0')}-${String(f.getDate()).padStart(2, '0')}`;
    }

    function actualizarVisuales() {
      // Limpiar todas las clases previas
      const celdas = document.querySelectorAll('.calendario td');
      celdas.forEach(td => td.classList.remove('dia-seleccionado', 'rango'));

      if (!fechaInicio) return;

      // Recorrer todas las celdas válidas
      const tds = document.querySelectorAll('.calendario td:not(.dia-no-disponible)');
      tds.forEach(td => {
        const dia = parseInt(td.textContent);
        if (isNaN(dia)) return;


        const bloqueMes = td.closest('.mes');
        if (!bloqueMes) return;

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
      mesActual--; if (mesActual < 0) { mesActual = 11; anoActual--; }
      renderCalendarios();
    };

    document.getElementById('btnNext').onclick = () => {
      mesActual++; if (mesActual > 11) { mesActual = 0; anoActual++; }
      renderCalendarios();
    };

    renderCalendarios();