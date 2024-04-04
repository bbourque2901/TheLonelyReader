import MusicPlaylistClient from '../api/musicPlaylistClient';
import BindingClass from "../util/bindingClass";
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view user booklists page of the website.
 */
  class ViewUserBooklists extends BindingClass {
     constructor() {
             super();
             this.bindClassMethods(['clientLoaded', 'mount', 'addBooklistsToPage'], this);
             this.dataStore = new DataStore();
             console.log("viewUserBooklists constructor");
     }

 /**
  * Once the client is loaded, get the booklist metadata and book list.
  */
 async clientLoaded() {
     const urlParams = new URLSearchParams(window.location.search);
     const customerId = urlParams.get('email');
     document.getElementById('booklists').innerText = "Loading Lists ...";
     const booklists = await this.client.getUserBooklists(customerId);
     this.dataStore.set('booklists', booklists);
 }

 /**
  * Load the MusicPlaylistClient.
  */
  mount() {
      this.client = new MusicPlaylistClient();
      this.clientLoaded();
  }

  /**
   * When the booklist is updated in the datastore, update the booklist metadata on the page.
   */
   addBooklistsToPage() {
        const booklists = this.dataStore.get('booklists');
        if (booklists == null) {
            return;
        }

//        document.getElementById('booklist-name').innerText = booklist.name;
//        document.getElementById('booklist-owner').innerText = booklist.customerId;

        let booklistsHtml = '<table id="booklists-table"><tr><th>Name</th><th>Book Count</th><th>Tags</th><th>Booklist Id</th></tr>';
        let booklist;
        for (booklist of booklists) {
            booklistsHtml += `
            <tr>
                <td>${booklist.name}</td>
                <td>${booklist.bookCount}</td>
                <td>${booklist.tags}</td>
                <td>${booklist.id}</td>
            </tr>`;
        }
        document.getElementById('booklists').innerHTML = booklistsHtml;
    }

}

 /**
  * Main method to run when the page contents have loaded.
  */
  const main = async () => {
        const viewuserbooklists = new ViewUserBooklists();
        viewuserbooklists.mount();
  };

  window.addEventListener('DOMContentLoaded', main);