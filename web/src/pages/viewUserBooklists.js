import BookTrackerClient from '../api/bookTrackerClient';
import BindingClass from "../util/bindingClass";
import Header from '../components/header';
import DataStore from "../util/DataStore";

/**
 * Logic needed for the view user booklists page of the website.
 */
  class ViewUserBooklists extends BindingClass {
     constructor() {
             super();
             this.bindClassMethods(['clientLoaded', 'mount', 'addBooklistsToPage', 'remove'], this);
             this.dataStore = new DataStore();
             console.log("viewUserBooklists constructor");
             this.header = new Header(this.dataStore);
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
     this.addBooklistsToPage();
 }

 /**
  * Load the BookTrackerClient.
  */
  mount() {
      document.getElementById('booklists').addEventListener("click", this.remove);

      this.client = new BookTrackerClient();
      this.clientLoaded();
      this.header.addHeaderToPage();
  }

  /**
   * When the booklist is updated in the datastore, update the booklist metadata on the page.
   */
   addBooklistsToPage() {
        const booklists = this.dataStore.get('booklists');

        if (booklists == null) {
            return;
        }

        let booklistsHtml = '<table id="booklists-table"><tr><th>Name</th><th>Book Count</th><th>Tags</th><th>Booklist Id</th><th>Remove Booklist</th></tr>';
        let booklist;
        for (booklist of booklists) {
            booklistsHtml += `
            <tr id= "${booklist.id}">
                <td>
                    <a href="booklist.html?id=${booklist.id}">${booklist.name}</a>
                </td>
                <td>${booklist.bookCount}</td>
                <td>${booklist.tags?.join(', ')}</td>
                <td>${booklist.id}</td>
                <td><button data-id="${booklist.id}" class="button remove-booklist">Remove ${booklist.name}</button></td>
            </tr>`;
        }

        document.getElementById('booklists').innerHTML = booklistsHtml;

        document.getElementById('booklist-owner').innerText = booklist.customerId;
    }

    /**
     * when remove button is clicked, removes booklist.
     */
     async remove(e) {
         const removeButton = e.target;
         if (!removeButton.classList.contains('remove-booklist')) {
              return;
         }

         removeButton.innerText = "Removing...";

         const errorMessageDisplay = document.getElementById('error-message');
         errorMessageDisplay.innerText = ``;
         errorMessageDisplay.classList.add('hidden');

         await this.client.removeBooklist(removeButton.dataset.id, (error) => {
           errorMessageDisplay.innerText = `Error: ${error.message}`;
           errorMessageDisplay.classList.remove('hidden');
         });

         document.getElementById(removeButton.dataset.id).remove()
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