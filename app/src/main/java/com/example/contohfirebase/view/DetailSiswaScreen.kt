package com.example.contohfirebase.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.contohfirebase.R
import com.example.contohfirebase.modeldata.Siswa
import com.example.contohfirebase.modeldata.toDetailSiswa
import com.example.contohfirebase.view.route.DestinasiDetail
import com.example.contohfirebase.viewmodel.DetailViewModel
import com.example.contohfirebase.viewmodel.PenyediaViewModel
import com.example.contohfirebase.viewmodel.StatusUIDetail
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailSiswaScreen(
    navigateToEditItem: (Long) -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DetailViewModel = viewModel(factory = PenyediaViewModel.Factory)
) {
    val coroutineScope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            SiswaTopAppBar(
                title = stringResource(DestinasiDetail.titleRes),
                canNavigateBack = true,
                navigateUp = navigateBack
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    val siswa = (viewModel.statusUIDetail as? StatusUIDetail.Success)?.satusiswa
                    siswa?.let { navigateToEditItem(it.id) }
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = stringResource(R.string.edit_siswa)
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        DetailBody(
            statusUIDetail = viewModel.statusUIDetail,
            onDelete = {
                coroutineScope.launch {
                    viewModel.hapusSatuSiswa()
                    navigateBack()
                }
            },
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        )
    }
}

@Composable
private fun DetailBody(
    statusUIDetail: StatusUIDetail,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    var deleteConfirmationRequired by rememberSaveable { mutableStateOf(false) }

    when (statusUIDetail) {
        is StatusUIDetail.Loading -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is StatusUIDetail.Error -> {
            Box(modifier = modifier, contentAlignment = Alignment.Center) {
                Text(text = "Error loading data")
            }
        }
        is StatusUIDetail.Success -> {
            Column(
                modifier = modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
            ) {
                statusUIDetail.satusiswa?.let { siswa ->
                    ItemDetailsCard(
                        siswa = siswa,
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedButton(
                        onClick = { deleteConfirmationRequired = true },
                        shape = MaterialTheme.shapes.small,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }

            if (deleteConfirmationRequired) {
                DeleteConfirmationDialog(
                    onDeleteConfirm = {
                        deleteConfirmationRequired = false
                        onDelete()
                    },
                    onDeleteCancel = { deleteConfirmationRequired = false },
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
fun ItemDetailsCard(
    siswa: Siswa,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium))
        ) {
            ItemDetailsRow(
                labelResId = R.string.nama,
                itemDetail = siswa.nama,
                modifier = Modifier.fillMaxWidth()
            )
            ItemDetailsRow(
                labelResId = R.string.alamat,
                itemDetail = siswa.alamat,
                modifier = Modifier.fillMaxWidth()
            )
            ItemDetailsRow(
                labelResId = R.string.telpon,
                itemDetail = siswa.telpon,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ItemDetailsRow(
    labelResId: Int,
    itemDetail: String,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        Text(
            text = stringResource(labelResId),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = itemDetail,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onDeleteConfirm: () -> Unit,
    onDeleteCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = { /* Do nothing */ },
        title = { Text(stringResource(R.string.attention)) },
        text = { Text(stringResource(R.string.delete_question)) },
        modifier = modifier,
        dismissButton = {
            TextButton(onClick = onDeleteCancel) {
                Text(stringResource(R.string.no))
            }
        },
        confirmButton = {
            TextButton(onClick = onDeleteConfirm) {
                Text(stringResource(R.string.yes))
            }
        }
    )
}