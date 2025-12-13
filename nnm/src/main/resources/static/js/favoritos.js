document.addEventListener("DOMContentLoaded", () => {
    const botones = document.querySelectorAll('.fav-btn');

    // Inicializar corazones según la lista de favoritos del usuario
    fetch('/favoritos/lista')
        .then(res => {
            if (res.status === 401) {
                window.location.href = '/login';
                return null;
            }
            return res.json(); // Array de IDs de favoritos
        })
        .then(favoritos => {
            if (!favoritos) return;

            botones.forEach(btn => {
                const id = parseInt(btn.dataset.id);
                if (favoritos.includes(id)) {
                    btn.classList.add('activo');
                    btn.textContent = '❤';
                } else {
                    btn.textContent = '♡';
                }
            });
        });

    // Toggle al hacer clic
    botones.forEach(btn => {
        btn.addEventListener('click', () => {
            const id = parseInt(btn.dataset.id);

            fetch('/favoritos/toggle', {
                method: 'POST',
                headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
                body: 'idInmueble=' + id
            })
            .then(res => {
                if (res.status === 401) {
                    window.location.href = '/login';
                    return null;
                }
                return res.text();
            })
            .then(data => {
                if (!data) return;
                if (data === "true") {
                    btn.classList.add('activo');
                    btn.textContent = '❤';
                } else {
                    btn.classList.remove('activo');
                    btn.textContent = '♡';
                }
            })
            .catch(err => console.error("Error al actualizar favorito:", err));
        });
    });
});
