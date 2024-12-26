This project is my lazily-done Chess-engine, which should beat your asses :3
 Author: Vovencio
 Version: 12/22/24


Notes for my demented self:
- The figures are represented by bytes in increasing order of value: P, N, B, R, Q, K.
> Whereas 0 represents an empty field.
> White pieces are 1-6; black are 7-12
- The bottom left corner is the coordinate origin, while the upper right corner is (7, 7).
- White is located at the bottom.
- IsPiece and other booleans are represented by bytes with 0 corresponding to False and 1 to true
- IsPiece is 0 for no piece, 1 for a white piece, 2 for a black piece
> This is made to make baking easier
- Squares have move lists, so that I can update all pieces locally and must not update the whole board
> This allows me to evaluate the position more positionally ;)