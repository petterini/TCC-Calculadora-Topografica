// header.js
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

    // Fechar menu ao clicar em links (melhor para mobile)
    const sidebarLinks = document.querySelectorAll('.sidebar-link');
    sidebarLinks.forEach(link => {
        link.addEventListener('click', closeSidebar);
    });

    // Menu de login
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

    // Fechar tudo ao pressionar ESC
    document.addEventListener('keydown', function(e) {
        if (e.key === 'Escape') {
            closeSidebar();
            loginMenu.classList.remove('active');
        }
    });

    // Prevenir problemas de scroll em mobile
    sidebar.addEventListener('touchmove', function(e) {
        e.stopPropagation();
    }, { passive: false });

    overlay.addEventListener('touchmove', function(e) {
        e.preventDefault();
    }, { passive: false });
});