# JavaInternetParsing
Some examples of internet parsing with java (some are Armenian).
* listamparser - is parsing Armenian website list.am
* menuamparser - is parsing Armenian website menu.am (already all is done at 17.07.2018)
* rawfacebookgroupparser - this is parsing facebook groups and saving posts' text. Raw, because this isn't using Facebook's Graph API, because they limited it's abilities, so it now can't get even public information from facebook pages, where you aren't admin. So to get the HTML file, you have to open the facebook page you want to parse (if it is big, then use incognito mode, to speed up the process and saving more RAM by preventing browser from saving cache) and run some scripts from the terminal to get to the bottom of the page with interval.
Here is an example.
```javascript
function scrollToTheBottom(){
    window.scrollTo(0, document.body.scrollHeight);
}
let intrvl = setInterval(scrollToTheBottom, 100);
```
This will get you to the bottom of the page (or the browser will breake, as happened with my chrome and only incognito mode helped). After all that just press Ctrl-S or Command-S to save the page's HTML, which the program will process.
