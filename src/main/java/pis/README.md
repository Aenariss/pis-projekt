# BookShop REST API
## Endpoints

### Category

- `GET /api/category` - get all categories,
- `GET /api/category/{id}` - get category by id,
- `POST /api/category` - add new category,
- `PUT /api/category/{id}` - update category by id,
- `DELETE /api/category/{id}` - delete category by id
- `DELETE /api/category` - delete category by name

### BookAuthor

- `GET /api/book-author` - get all book authors,
- `GET /api/book-author/{id}` - get book author by id,
- `POST /api/book-author` - add new book author,
- `PUT /api/book-author/{id}` - update book author by id,
- `DELETE /api/book-author/{id}` - delete book author by id
- `DELETE /api/book-author` - delete book author by first name and last name

### Discount

- `GET /api/discount` - get all discounts,
- `GET /api/discount/{id}` - get discount by id,
- `POST /api/discount` - add new discount,
- `PUT /api/discount/{id}` - update discount by id,
- `DELETE /api/discount/{id}` - delete discount by id
- `DELETE /api/discount` - delete discount by discount value

### Language

- `GET /api/language` - get all languages,
- `GET /api/language/{id}` - get language by id,
- `POST /api/language` - add new language,
- `PUT /api/language/{id}` - update language by id,
- `DELETE /api/language/{id}` - delete language by id
- `DELETE /api/language` - delete language by "language"

### ProductDescription

- `GET /api/productdescription` - get all product descriptions,
- `GET /api/productdescription/{id}` - get product description by id,
- `GET /api/productdescription/author/{authorId}` - get product description by author id,
- `GET /api/productdescription/language/{languageId}` - get product description by language id,
- `GET /api/productdescription/discount/{discountId}` - get product description by discount id,
- `GET /api/productdescription/category/{categoryId}` - get product description by category id
- `POST /api/productdescription/search` - get product description by query
 <span style="color:green">For now supports querrying text from ProductDescription.{name,description,ISBN}, BookAuthor.{firstname, surname}, Category.{name, description}, Language.language</span>
 <span style="color:red">Add something?</span>
 ```json
format -> {"query":"query_text"}
```
- `POST /api/productdescription/filter` - get product description by filters
```json
{
"authorIds": [id],
"languageIds": [id],
"categoryIds": [id],
"priceFrom": double,
"priceTo": double,
"pagesFrom": int,
"pagesTo": int,
"discountFrom": int,
"discountTo": int
}
```
 <span style="color:green">Values can be removed and will automatically be replaced with some min/max value so that the filtering works only with those set</span>
- `POST /api/productdescription` - add new product description,
- `PUT /api/productdescription/{id}` - update product description by id,
- `PUT /api/productdescription/{id}/author/{authorId}` - add author to product description,
- `PUT /api/productdescription/{id}/language/{languageId}` - add language to product description,
- `PUT /api/productdescription/{id}/discount/{discountId}` - add discount to product description,
- `PUT /api/productdescription/{id}/category/{categoryId}` - add category to product description,
- `PUT /api/productdescription/{id}/categories` - add categories to product description,
```json
format -> ["name1","name2"]
```
- `DELETE /api/productdescription/{id}` - delete product description by id
- `DELETE /api/productdescription` - delete product description by title <span style="color:red">Maybe by ISBN?</span>
- `DELETE /api/productdescription/{id}/author` - delete author from product description,
- `DELETE /api/productdescription/{id}/language` - delete language from product description,
- `DELETE /api/productdescription/{id}/discount` - delete discount from product description,
- `DELETE /api/productdescription/{id}/category/{categoryId}` - delete category with given id from product description,
- `DELETE /api/productdescription/{id}/categories` - delete categories from product description

### For development purposes

- `GET /api/prefill` - prefills db with data,

