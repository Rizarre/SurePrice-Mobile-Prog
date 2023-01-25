from bs4 import BeautifulSoup
import requests

def main(search):
    s = search
    link = 'https://smmarkets.ph/catalogsearch/result/?q=' + s + '&p=1&___store=sm_supermarket_baguio'
    cnt = 0
    html_text = requests.get(link).text
    soup = BeautifulSoup(html_text, 'html.parser')
    info = soup.find_all('li', {"class": "product-item col-xs-5ths col-sm-4 col-xs-6 col-xs-custom"})
    result = [" ", " ", " "], [" ", " ", " "], [" ", " ", " "], [" ", " ", " "], [" ", " ", " "]
    row = 0
    for item in info:
        title = item.find('a', {'id': 'product_name'}).text.strip()
        price = item.find('span', {'class', 'price'}).text.strip()
        image = item.find('img' , attrs = {'class':'product-image-photo img-responsive'})
        image = image['src']
        result[row][0] = title
        result[row][1] = price
        result[row][2] = image
        row += 1
        cnt = cnt + 1
        if cnt == 5:
            break
        
    return result