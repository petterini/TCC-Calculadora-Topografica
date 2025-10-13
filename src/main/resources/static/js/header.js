// Configuração do menu mobile
document.addEventListener('DOMContentLoaded', function() {
    const menuToggle = document.getElementById('menuToggle');
    const closeMenu = document.getElementById('closeMenu');
    const sidebar = document.getElementById('sidebar');
    const overlay = document.getElementById('overlay');
    const loginToggle = document.getElementById('loginToggle');
    const loginMenu = document.getElementById('loginMenu');

    // Abrir menu lateral
    menuToggle.addEventListener('click', function() {
        sidebar.classList.add('open');
        overlay.classList.add('active');
        document.body.style.overflow = 'hidden';
    });

    // Fechar menu lateral
    function closeSidebar() {
        sidebar.classList.remove('open');
        overlay.classList.remove('active');
        document.body.style.overflow = '';
    }

    closeMenu.addEventListener('click', closeSidebar);
    overlay.addEventListener('click', closeSidebar);

    // Toggle menu de login
    loginToggle.addEventListener('click', function(e) {
        e.stopPropagation();
        loginMenu.classList.toggle('active');
    });

    // Fechar menu de login ao clicar fora
    document.addEventListener('click', function(e) {
        if (!loginToggle.contains(e.target) && !loginMenu.contains(e.target)) {
            loginMenu.classList.remove('active');
        }
    });

    // Fechar menu ao pressionar ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeSidebar();
            loginMenu.classList.remove('active');
        }
    });

    // Fechar menu ao redimensionar a janela (para desktop)
    window.addEventListener('resize', function() {
        if (window.innerWidth > 768) {
            closeSidebar();
        }
    });
});