# Proyecto: BuscaLigasExternas<br>
Autor: Oscar Alberto Salgado Cárdenas<br>
email: oscasc@gmail.com<br><br>

Aplicación de consola programada en Java. Se utiliza para buscar en el código de un proyecto (pensado para proyectos JEE), referencias a ligas externas tales como http/https/email/ftp/C:/D:/... en archivos .java .js .html .htm .properties, etc. y presenta los resultados en una hoja de excel, agrupando los URLs encontrados, los archivos donde se localizaron y una línea de código de ejemplo; por nombre de servidor.<br>

IMPORTANTE: Cuando se localiza un url y se revisa su código de ejemplo, si éste pertenece a un comentario, ignora la referencia. Sin embargo, paa deterctar que el código de ejemplo es parte de un comentario, la línea detectada, debe preceder los caracteres de comentarios: /*, *, //, <!--  etc. Si el código detectado, pertenece a un comentario, pero carece de esta condición, la aplicación no lo detecta como comentario, ya que no realiza análisis léxico del código.<br>

<b>Mejoras pendientes:</b><br>

1-Cuando un archivo contiene el patrón, pero se identifica como código comentado, se sigue reportando el URL y el archivo. En la próxima revisión se corregirá ese problema. Sólo se reportará código y url que contengan código activo.<br>

2-Cuando en un mismo renglón, se presenta más de un patrón, sólo se considera al primero e ignora a los demás. 
