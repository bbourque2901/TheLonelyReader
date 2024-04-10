import BookTrackerClient from '../api/bookTrackerClient';
import BindingClass from "../util/bindingClass";

/**
 * The header component for the website.
 */
export default class Header extends BindingClass {
    constructor() {
        super();

        const methodsToBind = [
            'addHeaderToPage', 'createSiteTitle', 'createMenuHeader', 'createUserInfoForHeader',
            'createLoginButton', 'createLoginButton', 'createLogoutButton'
        ];
        this.bindClassMethods(methodsToBind, this);

        this.client = new BookTrackerClient();
    }

    /**
     * Add the header to the page.
     */
    async addHeaderToPage() {
        const currentUser = await this.client.getIdentity();

        const siteTitle = this.createSiteTitle();
        const menuHeader = this.createMenuHeader();
        const userInfo = this.createUserInfoForHeader(currentUser);

        const header = document.getElementById('header');
        header.appendChild(siteTitle);
        header.appendChild(menuHeader);
        header.appendChild(userInfo);
    }

    createSiteTitle() {
        const homeButton = document.createElement('a');
        homeButton.classList.add('header_home');
        homeButton.href = 'index.html';

        const imageButton = document.createElement('img');
        imageButton.src = "css/header new.png";
        imageButton.width = "400";
        homeButton.appendChild(imageButton);

        const siteTitle = document.createElement('div');
        siteTitle.classList.add('site-title');
        siteTitle.appendChild(homeButton);

        return siteTitle;
    }
//base header that the other buttons are added to
    createMenuHeader() {
        const menuList = document.createElement('ul');
        menuList.classList.add('menu_list');

        menuList.appendChild(this.createViewBooklistLink());
        menuList.appendChild(this.createNewBooklistLink());

        const menu = document.createElement('div');
        menu.classList.add('menu');
        menu.appendChild(menuList);

        return menu;
    }
//viewbooklist header button
    createViewBooklistLink() {
        const viewBooklistLink = document.createElement('a');
        viewBooklistLink.href = 'userBooklists.html';
        const viewBooklist = document.createElement('li');
        viewBooklist.classList.add('nav');
        viewBooklist.innerText = 'My Booklists';
        viewBooklistLink.appendChild(viewBooklist);
        return viewBooklistLink;
    }
//create new booklist button
    createNewBooklistLink() {
        const newBooklistLink = document.createElement('a');
        newBooklistLink.href = 'createBooklist.html';
        const newBooklist = document.createElement('li');
        newBooklist.classList.add('nav');
        newBooklist.innerText = 'New Booklist';
        newBooklistLink.appendChild(newBooklist);
        return newBooklistLink;
    }

    createUserInfoForHeader(currentUser) {
        const userInfo = document.createElement('div');
        userInfo.classList.add('user');

        const childContent = currentUser
            ? this.createLogoutButton(currentUser)
            : this.createLoginButton();

        userInfo.appendChild(childContent);

        return userInfo;
    }

    createLoginButton() {
        return this.createButton('Login', this.client.login);
    }

    createLogoutButton(currentUser) {
        return this.createButton(`Logout: ${currentUser.name}`, this.client.logout);
    }

    createButton(text, clickHandler) {
        const button = document.createElement('a');
        button.classList.add('button');
        button.href = '#';
        button.innerText = text;

        button.addEventListener('click', async () => {
            await clickHandler();
        });

        return button;
    }
}
