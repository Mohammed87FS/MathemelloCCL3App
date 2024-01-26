
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.cc221045.mathemelloccl3.data.Request
import com.cc221045.mathemelloccl3.ui.theme.appFontFamily
import com.cc221045.mathemelloccl3.viewmodel.MainViewModel
import com.google.firebase.auth.FirebaseAuth


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestsListScreen(viewModel: MainViewModel, userEmail: String, isAdmin: Boolean) {
    var searchQuery by remember { mutableStateOf("") }
    var allRequests by remember { mutableStateOf(listOf<Request>()) }
    var filteredRequests by remember { mutableStateOf(listOf<Request>()) }

    val darkBackground = Color(4279705391) // Replace with the exact color from the screenshot
    val textColor = Color(0xFF60A491)
    val simpleTextColor = Color(0xFF9D9EA5)
    val buttonColor = Color(0xFF3C3F4A) // Replace with the exact button color from the screenshot
    val cornerRadius = 10.dp

    LaunchedEffect(userEmail, isAdmin) {
        allRequests = if (FirebaseAuth.getInstance().currentUser?.email =="admin@admin.com") {
            viewModel.getAllRequests()
        } else {
            viewModel.getUserRequests(userEmail)
        }
        filteredRequests = allRequests
    }
    LaunchedEffect(searchQuery) {
        filteredRequests = if (searchQuery.isEmpty()) {
            allRequests
        } else {
            allRequests.filter {
                it.title.contains(searchQuery, ignoreCase = true) ||
                        it.content.contains(searchQuery, ignoreCase = true)
            }
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(darkBackground) // Apply the background color here
    )
    Column(modifier = Modifier.padding(16.dp)) {
        Text(
            text = "Requests",
            color = textColor,
            style = TextStyle(
                fontFamily = appFontFamily, // or any other font family you want
                fontWeight = FontWeight.ExtraBold, // choose the desired weight
                fontSize = 32.sp // set the font size as needed
            ),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search requests", color = simpleTextColor,
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Medium) },
            trailingIcon = { // This is the change
                Icon(
                    imageVector = Icons.Filled.Search, // Assuming you have a Search icon in your material icons
                    contentDescription = "Search",
                    tint = simpleTextColor,
                    modifier = Modifier.clickable { /* Icon click logic here */ }
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(cornerRadius)
        )


        if (filteredRequests.isEmpty()) {
            Text("No requests available", color = simpleTextColor,
                fontFamily = appFontFamily,
                fontWeight = FontWeight.Medium)
        } else {
            LazyColumn {
                items(filteredRequests) { request ->
                    if (FirebaseAuth.getInstance().currentUser?.email !="admin@admin.com"){
                        UserRequestItem(request,viewModel)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                    else{
                        AdminRequestItem(request, viewModel,
                            onCheckClicked = { viewModel.reloadRequests() })
                        Spacer(modifier = Modifier.height(8.dp))

                    }

                }

            }
        }
    }
}



@Composable
fun AdminRequestItem(request: Request, viewModel: MainViewModel,  onCheckClicked: () -> Unit) {

    var isChecked by remember { mutableStateOf(false) }

    val simpleTextColor = Color(4284375919)
    LaunchedEffect(request.requestId) {
        isChecked = viewModel.isRequestChecked(request.requestId)
        Log.d("AdminRequestItem", "LaunchedEffect triggered for requestId: ${request.requestId}, isChecked: $isChecked")
    }
    val imagePainter = rememberAsyncImagePainter(model = request.imageUrl)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(4280626236)) // Set the card background color here

    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(request.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(request.content, style = MaterialTheme.typography.bodySmall  )

            Spacer(modifier = Modifier.width(24.dp))
// Image
            request.imageUrl?.let { imageUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp), // Define the shape of the card
                    elevation = CardDefaults.cardElevation(4.dp)

                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(150.dp) // Set a fixed height
                            .fillMaxWidth() // Ensure it fills the width of the card
                            .clip(RoundedCornerShape(8.dp)) // Clip the image to fit the card shape
                            .aspectRatio(1f), // Maintain aspect ratio
                        contentScale = ContentScale.Crop // Crop the image to fit the dimensions
                    )

                    if (imagePainter.state is AsyncImagePainter.State.Error) {
                        val errorState = imagePainter.state as AsyncImagePainter.State.Error
                        val throwable = errorState.result.throwable
                        Log.e("PostItem", "Error loading image: ${throwable.message}")
                    }
                    Spacer(modifier = Modifier.width(24.dp)) // Add space between image and text

                }
                androidx.compose.material.Text(
                    buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = simpleTextColor,
                                fontFamily = appFontFamily,
                                fontWeight = FontWeight.Medium, fontSize = 10.sp
                            )
                        ) {
                            append("Email: ${request.userEmail}")
                        }

                    }
                )


                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {

                IconButton(
                    onClick = {
                        isChecked=!isChecked
                        viewModel.toggleCheckedRequest(request.requestId)
                        onCheckClicked()
                    },
                ) {
                    Icon(
                        imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Filled.Clear,
                        contentDescription = if (isChecked) "Uncheck" else "Check",
                        tint = if (isChecked) Color(0xFF60A491) else Color(0xFFFF5722)
                    )
                }}

            }



        }
    }
}




@Composable
fun UserRequestItem(request: Request,viewModel: MainViewModel) {
    var isChecked by remember { mutableStateOf(false) }


    LaunchedEffect(request.requestId) {
        isChecked = viewModel.isRequestChecked(request.requestId)
        Log.d("AdminRequestItem", "LaunchedEffect triggered for requestId: ${request.requestId}, isChecked: $isChecked")
    }

    val imagePainter = rememberAsyncImagePainter(model = request.imageUrl)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(4280626236))





    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(request.title, style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(request.content, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(modifier = Modifier.width(16.dp))
// Image
            request.imageUrl?.let { imageUrl ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    shape = RoundedCornerShape(8.dp), // Define the shape of the card
                    elevation = CardDefaults.cardElevation(4.dp)

                ) {
                    Image(
                        painter = imagePainter,
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .height(150.dp) // Set a fixed height
                            .fillMaxWidth() // Ensure it fills the width of the card
                            .clip(RoundedCornerShape(8.dp)) // Clip the image to fit the card shape
                            .aspectRatio(1f), // Maintain aspect ratio
                        contentScale = ContentScale.Crop // Crop the image to fit the dimensions
                    )

                    if (imagePainter.state is AsyncImagePainter.State.Error) {
                        val errorState = imagePainter.state as AsyncImagePainter.State.Error
                        val throwable = errorState.result.throwable
                        Log.e("PostItem", "Error loading image: ${throwable.message}")
                    }
                    Spacer(modifier = Modifier.width(16.dp)) // Add space between image and text

                }


                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth(),
                ) {


                    Icon(
                        imageVector = if (isChecked) Icons.Filled.CheckCircle else Icons.Filled.Clear,
                        contentDescription = if (isChecked) "Uncheck" else "Check",
                        tint = if (isChecked) Color(0xFF60A491) else Color(0xFFFF5722)
                    )
                }}

            }

        }
    }

