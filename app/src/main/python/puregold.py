from bs4 import BeautifulSoup
import requests

def main(search):
    s = search
    link = 'https://purego.ph/search?type=product&q=' + s + '%20tag:store_510'
    cnt = 0
    html_text = requests.get(link).text
    soup = BeautifulSoup(html_text, 'html.parser')
    info = soup.find_all('div', {"class": "card product-card"})
    result = [" ", " ", " "], [" ", " ", " "], [" ", " ", " "], [" ", " ", " "], [" ", " ", " "]
    row = 0
    for item in info:
        title = item.find('h5', {'class': 'card-title is-web'}).text.strip()
        price = item.find('p', {'class', 'card-text'}).text.strip()
        image = item.find('img', attrs={'class': 'card-img-top'})
        image = image['src']
        result[row][0] = title
        result[row][1] = price
        result[row][2] = "https:" + image
        row += 1
        cnt = cnt + 1
        if cnt == 5:
            break
    return result
#print(main("meat"))
